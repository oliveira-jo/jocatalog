package com.devjoliveira.jocatalog.dtos;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.devjoliveira.jocatalog.entities.Category;
import com.devjoliveira.jocatalog.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductDTO(
    Long id,
    @Size(min = 5, max = 60, message = "Name must be between 5 and 60 characters") @NotBlank(message = "Name is required") String name,
    @NotBlank(message = "Description is required") String description,
    @Positive(message = "Price must be positive") Double price,
    String imgUrl,
    @PastOrPresent(message = "Date cannot be in the future") Instant date,
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
