package com.devjoliveira.jocatalog.controllers.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devjoliveira.jocatalog.services.exceptions.DatabaseException;
import com.devjoliveira.jocatalog.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<StandartError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    StandartError error = new StandartError(
        Instant.now(),
        status.value(),
        "Resource not found.",
        e.getMessage(),
        request.getRequestURI());
    return ResponseEntity.status(status).body(error);
  }

  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<StandartError> databaseException(DatabaseException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    StandartError error = new StandartError(
        Instant.now(),
        status.value(),
        "Database exception.",
        e.getMessage(),
        request.getRequestURI());
    return ResponseEntity.status(status).body(error);
  }

}
