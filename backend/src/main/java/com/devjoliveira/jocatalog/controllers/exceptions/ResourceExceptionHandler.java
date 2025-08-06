package com.devjoliveira.jocatalog.controllers.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devjoliveira.jocatalog.services.exceptions.EntityNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<StandartError> entityNotFound(EntityNotFoundException e, HttpServletRequest request) {
    StandartError error = new StandartError(
        Instant.now(),
        HttpStatus.NOT_FOUND.value(),
        "Resource not found",
        e.getMessage(),
        request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

}
