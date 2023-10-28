package io.github.LuizMartendal.library.exceptions;

import io.github.LuizMartendal.library.exceptions.especifics.BadRequestException;
import io.github.LuizMartendal.library.exceptions.especifics.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerInterceptor {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandError> exception(Exception e) {
        return ResponseEntity.ok().body(new StandError(
                HttpStatus.INTERNAL_SERVER_ERROR.name(), 
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                System.currentTimeMillis()
        ));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandError> badRequestException(BadRequestException e) {
        return ResponseEntity.ok().body(new StandError(
                HttpStatus.BAD_REQUEST.name(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis()
        ));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandError> NotFoundException(NotFoundException e) {
        return ResponseEntity.ok().body(new StandError(
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
