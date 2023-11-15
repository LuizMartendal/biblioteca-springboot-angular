package io.github.LuizMartendal.library.exceptions.especifics;

import org.springframework.security.core.AuthenticationException;

import java.io.Serializable;

public class JwtExpiredException extends AuthenticationException implements Serializable {

    private static final long serialVersionUID = 1L;

    public JwtExpiredException(String message) {
        super(message);
    }
}
