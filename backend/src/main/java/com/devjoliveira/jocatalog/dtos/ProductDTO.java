package com.devjoliveira.jocatalog.dtos;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.devjoliveira.jocatalog.entities.Category;
import com.devjoliveira.jocatalog.entities.Product;

public record ProductDTO(
    Long id,
    String name,
    String description,
    Double price,
    String imgUrl,
    Instant date,
    List<CategoryDTO> categories) {

  public ProductDTO(Product product) {
    this(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getImgUrl(),
        product.getDate(),
        List.of());
  }

  public ProductDTO(Product product, Set<Category> categories) {
    this(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getImgUrl(),
        product.getDate(),
        categories.stream()
            .map(obj -> new CategoryDTO(obj.getId(), obj.getName()))
            .collect(Collectors.toList()));
  }
}
