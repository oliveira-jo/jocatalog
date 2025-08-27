package com.devjoliveira.jocatalog.controllers;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
import com.devjoliveira.jocatalog.dtos.UserMinDTO;
import com.devjoliveira.jocatalog.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<Page<UserMinDTO>> findAll(Pageable pageable) {
    return ResponseEntity.ok().body(userService.findAllPaged(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserMinDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok().body(userService.findById(id));
  }

  @PostMapping
  public ResponseEntity<UserMinDTO> save(@Valid @RequestBody UserDTO dto) {
    UserMinDTO min = userService.save(dto);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(min.id()).toUri();

    return ResponseEntity.created(uri).body(min);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserMinDTO> change(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
    return ResponseEntity.ok().body(userService.update(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
