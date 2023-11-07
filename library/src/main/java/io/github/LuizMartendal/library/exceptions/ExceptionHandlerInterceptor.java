package io.github.LuizMartendal.library.exceptions;

import io.github.LuizMartendal.library.exceptions.especifics.BadRequestException;
import io.github.LuizMartendal.library.exceptions.especifics.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
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

    @ExceptionHandler({AccessDeniedException.class, InsufficientAuthenticationException.class})
    public ResponseEntity<StandError> userServerError(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new StandError(
                        HttpStatus.UNAUTHORIZED.name(),
                        "You cannot access this service",
                        HttpStatus.UNAUTHORIZED.value(),
                        System.currentTimeMillis()
                )
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandError> badRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandError(
                HttpStatus.BAD_REQUEST.name(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis()
        ));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandError> NotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StandError(
                HttpStatus.NOT_FOUND.name(),
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
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
}
