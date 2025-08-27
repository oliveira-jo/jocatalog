package com.devjoliveira.jocatalog.dtos;

import com.devjoliveira.jocatalog.entities.Category;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(
    Long id,
    @NotBlank(message = "Name is required") String name) {

  public CategoryDTO(Category category) {
    this(category.getId(), category.getName());
  }

}
