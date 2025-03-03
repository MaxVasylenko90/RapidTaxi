package dev.mvasylenko.rapidtaxi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUserException(DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("error", "User already exists!"));
    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException exception) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(Collections.singletonMap("error", exception.getMessage()));
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception exception) {
        LOG.error("ERROR HAPPENED: ", exception);
        LOG.error("Exception class: {}", exception.getClass().getName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "Something went wrong!"));
    }
}
