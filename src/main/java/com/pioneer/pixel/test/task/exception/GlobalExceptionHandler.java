package com.pioneer.pixel.test.task.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.AuthenticationException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "Validation failed",
                                request.getRequestURI(),
                                errors);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI(),
                                null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAccessDenied(
                        org.springframework.security.access.AccessDeniedException ex,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.FORBIDDEN.value(),
                                HttpStatus.FORBIDDEN.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI(),
                                null);
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleException(Exception ex,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                "An unexpected error occurred",
                                request.getRequestURI(),
                                null);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI(),
                                null);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "Database constraint violation",
                                request.getRequestURI(),
                                null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
}