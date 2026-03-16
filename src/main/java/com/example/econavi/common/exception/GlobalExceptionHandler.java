package com.example.econavi.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, String>> methodArgumentNotValidException(
            final MethodArgumentNotValidException e, final HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}, message : {}",
                e, request.getRequestURI(), e.getMessage());

        HashMap<String, String> errors = new HashMap<>();

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> exception(
            ApiException e, HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}, message : {}",
                e, request.getRequestURI(), e.getMessage());

        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
/*
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(
            Exception e, HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}, message : {}",
                e, request.getRequestURI(), e.getMessage());

        return ResponseEntity.internalServerError().body(e.getMessage());
    }
 */
}
