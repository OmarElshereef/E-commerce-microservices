package com.OmarElshereef.authservice.auth.controller;


import com.OmarElshereef.authservice.auth.dto.response.AuthenticationResponse;
import com.OmarElshereef.authservice.auth.security.TokenBlacklistService;
import com.OmarElshereef.authservice.auth.security.jwt.utils.JwtUtils;
import com.OmarElshereef.authservice.auth.service.AuthenticationService;
import com.OmarElshereef.authservice.auth.dto.request.LoginRequest;
import com.OmarElshereef.authservice.auth.dto.request.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final JwtUtils jwtUtils;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromHeader(request);

        if (token != null) {
            tokenBlacklistService.blacklistToken(token);
            return ResponseEntity.ok("Logout successful");
        }

        return ResponseEntity.badRequest().body("No token found");
    }


}
