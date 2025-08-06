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

import com.devjoliveira.jocatalog.dtos.CategoryDTO;
import com.devjoliveira.jocatalog.services.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable) {
    return ResponseEntity.ok().body(categoryService.findAllPaged(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok().body(categoryService.findById(id));
  }

  @PostMapping
  public ResponseEntity<CategoryDTO> save(@RequestBody CategoryDTO categoryDTO) {
    categoryDTO = categoryService.save(categoryDTO);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(categoryDTO.id()).toUri();

    return ResponseEntity.created(uri).body(categoryDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryDTO> change(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
    return ResponseEntity.ok().body(categoryService.update(id, categoryDTO));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    categoryService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
