package com.OmarElshereef.apigateway.utils.exception;

public class JwtTokenMissingException extends RuntimeException {
    public JwtTokenMissingException(String message, Throwable cause) {
        super(message, cause);
    }
    public JwtTokenMissingException(String message) {
        super(message);
    }
}