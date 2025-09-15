package com.OmarElshereef.authservice.auth.service;


import com.OmarElshereef.authservice.auth.exception.UserAlreadyExistsException;
import com.OmarElshereef.authservice.auth.exception.UserRegistrationException;
import com.OmarElshereef.authservice.model.User;
import com.OmarElshereef.authservice.repository.UserRepository;
import com.OmarElshereef.authservice.model.UserRole;
import com.OmarElshereef.authservice.auth.dto.response.AuthenticationResponse;
import com.OmarElshereef.authservice.auth.dto.request.LoginRequest;
import com.OmarElshereef.authservice.auth.dto.request.RegisterRequest;
import com.OmarElshereef.authservice.auth.exception.AccountNotFoundException;
import com.OmarElshereef.authservice.auth.exception.InvalidCredentialsException;
import com.OmarElshereef.authservice.auth.security.jwt.utils.JwtUtils;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        validateRegisterRequest(request);

        Optional<User> existingUser = repository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            log.warn("Registration attempt failed - user already exists: {}", request.getEmail());
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }
        try{
            var user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(UserRole.CUSTOMER)
                    .created_at(LocalDateTime.now())
                    .build();

            repository.save(user);

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .role(user.getRole())
                    .build();

        } catch (
        DataIntegrityViolationException ex) {
            log.error("Database constraint violation during registration: {}", ex.getMessage());
            throw new UserRegistrationException("Registration failed due to data constraints");
        } catch (Exception ex) {
            log.error("Unexpected error during user registration: {}", ex.getMessage(), ex);
            throw new UserRegistrationException("Registration failed due to system error");
        }
    }

    public AuthenticationResponse login(LoginRequest request) {

        validateLoginRequest(request);

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account with email " + request.getEmail() + " not found"));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Wrong password");
        }

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Registration request cannot be null");
        }

        if (StringUtils.isBlank(request.getName())) {
            throw new IllegalArgumentException("Name is required");
        }

        if (StringUtils.isBlank(request.getEmail())) {
            throw new IllegalArgumentException("Email is required");
        }

        if (!isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (StringUtils.isBlank(request.getPassword())) {
            throw new IllegalArgumentException("Password is required");
        }

    }

    private boolean isValidEmail(String email) {
        return email != null &&
                email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private void validateLoginRequest(LoginRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Login request cannot be null");
        }

        if (StringUtils.isBlank(request.getEmail())) {
            throw new IllegalArgumentException("Email is required");
        }

        if (StringUtils.isBlank(request.getPassword())) {
            throw new IllegalArgumentException("Password is required");
        }
    }

}
