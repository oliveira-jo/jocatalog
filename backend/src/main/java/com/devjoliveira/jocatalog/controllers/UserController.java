package com.devjoliveira.jocatalog.controllers;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devjoliveira.jocatalog.dtos.UserDTO;
import com.devjoliveira.jocatalog.dtos.UserInsertDTO;
import com.devjoliveira.jocatalog.dtos.UserUpdateDTO;
import com.devjoliveira.jocatalog.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
  @GetMapping
  public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
    return ResponseEntity.ok().body(userService.findAllPaged(pageable));
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok().body(userService.findById(id));
  }

  @PostMapping
  public ResponseEntity<UserDTO> save(@Valid @RequestBody UserInsertDTO dto) {
    UserDTO min = userService.save(dto);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(min.getId()).toUri();

    return ResponseEntity.created(uri).body(min);
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> change(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
    return ResponseEntity.ok().body(userService.update(id, dto));
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
