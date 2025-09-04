package com.devjoliveira.jocatalog.servicies;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocatalog.dtos.ProductDTO;
import com.devjoliveira.jocatalog.repositories.ProductRepository;
import com.devjoliveira.jocatalog.services.ProductService;
import com.devjoliveira.jocatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {

  @Autowired
  private ProductService service;

  @Autowired
  private ProductRepository repository;

  private Long existingId;
  private Long nonExistingId;
  private Long totalProducts;

  @BeforeEach
  void setUp() throws Exception {

    existingId = 1L;
    nonExistingId = 1000L;
    totalProducts = 25L;

  }

  @Test
  public void findAllPagedShouldReturnPageWhenPageableIsValid() {

    PageRequest pageable = PageRequest.of(0, 10);
    Page<ProductDTO> result = service.findAllPaged("0", "", pageable);

    Assertions.assertNotNull(result);
    // or
    Assertions.assertFalse(result.isEmpty());

    Assertions.assertEquals(0, result.getNumber());
    Assertions.assertEquals(10, result.getSize());
    Assertions.assertEquals(totalProducts, result.getTotalElements());

  }

  @Test
  public void findAllPagedShouldReturnEmptyPageWhenPagedoesNotExist() {

    PageRequest pageable = PageRequest.of(50, 10);
    Page<ProductDTO> result = service.findAllPaged("0", "", pageable);

    Assertions.assertTrue(result.isEmpty());

  }

  @Test
  public void findAllPagedShouldReturnSortedPageWhenSortedByName() {

    PageRequest pageable = PageRequest.of(0, 10, Sort.by("name"));
    Page<ProductDTO> result = service.findAllPaged("0", "", pageable);

    Assertions.assertFalse(result.isEmpty());
    Assertions.assertEquals("Macbook Pro", result.getContent().get(0).name());
    Assertions.assertEquals("PC Gamer", result.getContent().get(1).name());
    Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).name());

  }

  @Test
  public void deleteShouldDeleteProductWhenIdExists() {

    service.delete(existingId);
    Assertions.assertEquals(totalProducts - 1, repository.count());

  }

  @Test
  public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      service.delete(nonExistingId);
    });
  }

}
