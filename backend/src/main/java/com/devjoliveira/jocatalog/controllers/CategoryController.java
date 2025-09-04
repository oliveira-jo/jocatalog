package com.devjoliveira.jocatalog.controllers;

import java.net.URI;
import java.util.List;

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

import com.devjoliveira.jocatalog.dtos.CategoryDTO;
import com.devjoliveira.jocatalog.services.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public ResponseEntity<List<CategoryDTO>> findAll() {
    return ResponseEntity.ok().body(categoryService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok().body(categoryService.findById(id));
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
  @PostMapping
  public ResponseEntity<CategoryDTO> save(@Valid @RequestBody CategoryDTO categoryDTO) {
    categoryDTO = categoryService.save(categoryDTO);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(categoryDTO.id()).toUri();

    return ResponseEntity.created(uri).body(categoryDTO);
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
  @PutMapping("/{id}")
  public ResponseEntity<CategoryDTO> change(@Valid @PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
    return ResponseEntity.ok().body(categoryService.update(id, categoryDTO));
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    categoryService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
