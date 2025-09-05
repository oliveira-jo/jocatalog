package com.devjoliveira.jocatalog.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devjoliveira.jocatalog.dtos.EmailDTO;
import com.devjoliveira.jocatalog.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/recover-token")
  public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody EmailDTO body) {
    authService.createRecoverToken(body);
    return ResponseEntity.noContent().build();

  }

}
