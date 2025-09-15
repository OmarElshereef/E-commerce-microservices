package com.OmarElshereef.apigateway.filter;

import com.OmarElshereef.apigateway.utils.GatewayJwtUtils;
import com.OmarElshereef.apigateway.utils.TokenBlacklistService;
import com.OmarElshereef.apigateway.utils.exception.JwtTokenExpiredException;
import com.OmarElshereef.apigateway.utils.exception.JwtTokenInvalidException;
import com.OmarElshereef.apigateway.utils.exception.JwtTokenMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthenticationGatewayFilter implements GlobalFilter, Ordered {

    private final GatewayJwtUtils jwtUtils;
    private final TokenBlacklistService tokenBlacklistService; // If you need this

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationGatewayFilter.class);

    public JwtAuthenticationGatewayFilter(GatewayJwtUtils jwtUtils,
                                          TokenBlacklistService tokenBlacklistService) {
        this.jwtUtils = jwtUtils;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();


        // Skip authentication for public endpoints
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }
        if (isPublicEndpoint(request.getURI().getPath()) || request.getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        try {
            String token = jwtUtils.getJwtFromHeader(request);

            if (token == null) {
                logger.debug("No JWT token found in request");
                return unauthorized(exchange);
            }

            // Check blacklist (if you're using it)
            if (tokenBlacklistService != null && tokenBlacklistService.isTokenBlacklisted(token)) {
                logger.debug("Token is blacklisted");
                return unauthorized(exchange);
            }

            if (!jwtUtils.isTokenValid(token)) {
                logger.debug("JWT token is invalid");
                return unauthorized(exchange);
            }

            // Extract user info and add to headers for downstream services
            String username = jwtUtils.extractUsername(token);
            List<String> roles = jwtUtils.extractRoles(token);

            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Username", username)
                    .header("X-User-Roles", String.join(",", roles))
                    .header("X-User-Token", token) // Pass token if downstream services need it
                    .build();

            logger.debug("JWT authentication successful for user: {}", username);
            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (JwtTokenExpiredException | JwtTokenInvalidException | JwtTokenMissingException e) {
            logger.debug("JWT exception occurred: {}", e.getMessage());
            return unauthorized(exchange);
        } catch (Exception e) {
            logger.error("Unexpected error in JWT filter: {}", e.getMessage(), e);
            return unauthorized(exchange);
        }
    }

    private boolean isPublicEndpoint(String path) {
        return path.contains("/auth/") ||
                path.contains("/eureka") ||
                path.equals("/") ||
                path.contains("/actuator/health");
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        // Optionally add error details
        String body = "{\"error\":\"Unauthorized\",\"message\":\"Invalid or missing JWT token\"}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        response.getHeaders().add("Content-Type", "application/json");

        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1; // Run before routing
    }
}