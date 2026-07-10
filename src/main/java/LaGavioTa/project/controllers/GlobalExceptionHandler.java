package LaGavioTa.project.controllers;

import LaGavioTa.project.util.errors.ObjectNotFoundException;
import LaGavioTa.project.util.errors.ResponseError;
import LaGavioTa.project.util.errors.UniqueValueException;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ResponseError> handleObjectNotFoundException(ObjectNotFoundException e) {
        ResponseError response = new ResponseError(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder errors = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.append(fieldError.getField()).append(" - ").append(fieldError.getDefaultMessage()).append(";");
        }
        ResponseError response = new ResponseError(errors.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UniqueValueException.class)
    public ResponseEntity<ResponseError> handleUniqueValueException(UniqueValueException e) {
        ResponseError response = new ResponseError(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseError> handleIllegalArgumentException(IllegalArgumentException e) {
        ResponseError response = new ResponseError(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseError> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        ResponseError response = new ResponseError(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

/// Добавить ошибку: DataIntegrityViolationException
