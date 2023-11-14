package io.github.LuizMartendal.library.exceptions.especifics;

public class JwtExpiredException extends RuntimeException {
    public JwtExpiredException(String message) {
        super(message);
    }
}
