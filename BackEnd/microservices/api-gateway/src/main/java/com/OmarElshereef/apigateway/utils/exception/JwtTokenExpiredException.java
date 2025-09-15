package com.OmarElshereef.apigateway.utils.exception;

public class JwtTokenExpiredException extends RuntimeException {
    public JwtTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}