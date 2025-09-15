package com.OmarElshereef.apigateway.utils.exception;

public class JwtTokenInvalidException extends RuntimeException {
    public JwtTokenInvalidException(String message) {
        super(message);
    }
    public JwtTokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
