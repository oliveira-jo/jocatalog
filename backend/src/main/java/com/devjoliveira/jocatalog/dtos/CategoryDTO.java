package com.devjoliveira.jocatalog.dtos;

import com.devjoliveira.jocatalog.entities.Category;

public record CategoryDTO(
    Long id,
    String name) {

  public CategoryDTO(Category category) {
    this(category.getId(), category.getName());
  }

}
