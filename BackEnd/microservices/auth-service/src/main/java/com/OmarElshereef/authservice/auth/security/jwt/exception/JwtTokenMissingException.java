package com.OmarElshereef.authservice.auth.security.jwt.exception;

public class JwtTokenMissingException extends RuntimeException {
    public JwtTokenMissingException(String message, Throwable cause) {
        super(message, cause);
    }
    public JwtTokenMissingException(String message) {
        super(message);
    }
}