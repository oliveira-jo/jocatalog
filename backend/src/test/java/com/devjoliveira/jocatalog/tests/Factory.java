package com.devjoliveira.jocatalog.tests;

import java.time.Instant;

import com.devjoliveira.jocatalog.dtos.ProductDTO;
import com.devjoliveira.jocatalog.entities.Category;
import com.devjoliveira.jocatalog.entities.Product;

public class Factory {

  public static Product createProduct() {

    Product product = new Product(1L,
        "Phone", "Good phone product", 800.0, "http://example.com/image.jpg", Instant.parse(Instant.now().toString()));

    product.getCategories().add(new Category(2L, "Electronics"));

    return product;
  }

  public static ProductDTO createProductDTO() {
    Product product = createProduct();
    return new ProductDTO(product, product.getCategories());
  }

  public static Category createCategory() {
    return new Category(2L, "Electronics");
  }

}
