package com.savoirstrategie.app.exception;

import org.springframework.security.core.AuthenticationException;

public class UnverifiedException extends AuthenticationException {

    public UnverifiedException(String message) {
        super(message);
    }
    public UnverifiedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
