package com.devjoliveira.jocatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.devjoliveira.jocatalog.entities.Product;
import com.devjoliveira.jocatalog.tests.Factory;

@DataJpaTest
public class ProductTests {

  @Autowired
  private ProductRepository repository;

  private Long existingId;
  private Long nonExistingId;
  private Long countTotalProducts;

  @BeforeEach
  void setUp() throws Exception {
    existingId = 1L;
    nonExistingId = 1000L;
    countTotalProducts = 25L;
  }

  @Test
  public void findByIdShouldReturnOptionalProductWhenIdExists() {

    Optional<Product> result = repository.findById(existingId);
    Assertions.assertTrue(result.isPresent());
    Assertions.assertTrue(result.get().getId().equals(existingId));

  }

  @Test
  public void findByIdShouldReturnEmptyOptionalWhenIdNotExists() {

    Optional<Product> result = repository.findById(nonExistingId);
    Assertions.assertFalse(result.isPresent());

  }

  @Test
  public void saveShouldPresisteWithAutoincrementWhenIdIsNull() {
    Product product = Factory.createProduct();
    product.setId(null);

    product = repository.save(product);

    Assertions.assertNotNull(product.getId());
    Assertions.assertEquals(countTotalProducts + 1, product.getId());
  }

  @Test
  public void deleteShouldDeleteProductWhenIdExists() {
    repository.deleteById(existingId);

    Optional<Product> result = repository.findById(existingId);
    Assertions.assertFalse(result.isPresent());
  }

}
