package io.github.LuizMartendal.library.exceptions;

import io.github.LuizMartendal.library.exceptions.especifics.BadRequestException;
import io.github.LuizMartendal.library.exceptions.especifics.JwtExpiredException;
import io.github.LuizMartendal.library.exceptions.especifics.NotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerInterceptor {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandError> exception(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandError(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "There´s a problem with the server. Try again later",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                System.currentTimeMillis()
        ));
    }

    @ExceptionHandler(JwtExpiredException.class)
    ResponseEntity<StandError> jwtExpiredException(JwtExpiredException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new StandError(
                        HttpStatus.FORBIDDEN.name(),
                        e.getMessage(),
                        HttpStatus.FORBIDDEN.value(),
                        System.currentTimeMillis()
                ));
    }

    @ExceptionHandler({AccessDeniedException.class, ExpiredJwtException.class, CredentialsExpiredException.class})
    ResponseEntity<StandError> securityException(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new StandError(
                        HttpStatus.FORBIDDEN.name(),
                        e.getMessage(),
                        HttpStatus.FORBIDDEN.value(),
                        System.currentTimeMillis()
                ));
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    ResponseEntity<StandError> insufficienteAuthenticationException(InsufficientAuthenticationException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new StandError(
                        HttpStatus.FORBIDDEN.name(),
                        "Your session is expired",
                        HttpStatus.FORBIDDEN.value(),
                        System.currentTimeMillis()
                ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<StandError> badCredentialsException(BadCredentialsException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN.value())
                .body(new StandError(
                        HttpStatus.FORBIDDEN.name(),
                        e.getMessage(),
                        HttpStatus.FORBIDDEN.value(),
                        System.currentTimeMillis()
                ));
    }

    @ExceptionHandler(TransactionSystemException.class)
    ResponseEntity<StandError> transactionalException(TransactionSystemException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new StandError(
                        HttpStatus.BAD_REQUEST.name(),
                        e.getCause().getCause().getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        System.currentTimeMillis()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<StandError> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        ValidationError validationError = new ValidationError(
                HttpStatus.BAD_REQUEST.name(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis());
        for (FieldError fieldError: e.getBindingResult().getFieldErrors()) {
            validationError.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationError);
    }

    @ExceptionHandler(BadRequestException.class)
    ResponseEntity<StandError> badRequestException(BadRequestException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new StandError(
                        HttpStatus.BAD_REQUEST.name(),
                        e.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        System.currentTimeMillis()
                ));
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<StandError> notFoundException(NotFoundException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new StandError(
                        HttpStatus.NOT_FOUND.name(),
                        e.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        System.currentTimeMillis()
                ));
    }

}
