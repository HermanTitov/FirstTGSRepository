package LaGavioTa.project.controllers;

import LaGavioTa.project.util.errors.ObjectNotFoundException;
import LaGavioTa.project.util.errors.ResponseError;
import LaGavioTa.project.util.errors.UniqueValueException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /// Ошибка: не найдена запись
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ResponseError> handleObjectNotFoundException(ObjectNotFoundException e) {
        log.warn("Business Exception [404 Not Found]: {}", e.getMessage());

        ResponseError response = new ResponseError(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /// Ошибка: невалидные значения (например, @Valid на DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder errors = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.append(fieldError.getField()).append(" - ").append(fieldError.getDefaultMessage()).append("; ");
        }

        String errorMessage = errors.toString().trim();
        log.warn("Validation Exception [400 Bad Request]: {}", errorMessage);

        ResponseError response = new ResponseError(errorMessage, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /// Ошибка: не уникальное значение
    @ExceptionHandler(UniqueValueException.class)
    public ResponseEntity<ResponseError> handleUniqueValueException(UniqueValueException e) {
        log.warn("Business Exception [400 Bad Request]: {}", e.getMessage());

        ResponseError response = new ResponseError(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /// Неверный аргумент сортировки
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseError> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Argument Exception [400 Bad Request]: {}", e.getMessage());

        ResponseError response = new ResponseError(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /// Ошибка базы данных (дубликаты на уровне DB констреинтов, null-значения и т.д.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseError> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        // Логируем как WARN, но пишем root cause ошибки, так как e.getMessage() у Spring Data слишком перегружен техническим текстом
        log.warn("Database Integrity Exception [400 Bad Request]: {}", e.getMostSpecificCause().getMessage());

        ResponseError response = new ResponseError("Database integrity violation: " + e.getMostSpecificCause().getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /// ГЛОБАЛЬНЫЙ ПЕРЕХВАТЧИК ВСЕХ ОСТАЛЬНЫХ ОШИБОК
    /// Сюда полетят NullPointerException, DB Connection Timeout и всё, что мы не обработали выше
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleAllUncaughtExceptions(Exception e) {
        // ВАЖНО: передаем сам объект 'e' последним аргументом БЕЗ {} — это заставит Slf4j напечатать весь Stack Trace в лог
        log.error("CRITICAL SYSTEM ERROR [500 Internal Server Error]: {}", e.getMessage(), e);

        ResponseError response = new ResponseError("An unexpected system error occurred. Please try again later.", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
