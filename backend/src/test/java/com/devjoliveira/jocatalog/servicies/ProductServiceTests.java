package com.devjoliveira.jocatalog.servicies;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devjoliveira.jocatalog.dtos.ProductDTO;
import com.devjoliveira.jocatalog.entities.Category;
import com.devjoliveira.jocatalog.entities.Product;
import com.devjoliveira.jocatalog.repositories.CategoryRepository;
import com.devjoliveira.jocatalog.repositories.ProductRepository;
import com.devjoliveira.jocatalog.services.ProductService;
import com.devjoliveira.jocatalog.services.exceptions.DatabaseException;
import com.devjoliveira.jocatalog.services.exceptions.ResourceNotFoundException;
import com.devjoliveira.jocatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

  @InjectMocks
  private ProductService service;

  @Mock
  private ProductRepository productRepositoryMock;

  @Mock
  private CategoryRepository categoryRepositoryMock;

  private Long existingId;
  private Long nonExistingId;
  private Long dependentId;
  private Product product;
  private ProductDTO productDTO;
  private PageImpl<Product> page;
  private Long categoryExistingId;
  private Category category;

  @BeforeEach
  void setUp() throws Exception {

    // Product
    existingId = 1L;
    nonExistingId = 1000L;
    dependentId = 3L;
    product = Factory.createProduct();
    productDTO = Factory.createProductDTO();
    page = new PageImpl<>(List.of(product));

    // Category
    categoryExistingId = 2L;
    category = Factory.createCategory();

    // Product
    Mockito.when(productRepositoryMock.existsById(existingId)).thenReturn(true);
    Mockito.when(productRepositoryMock.existsById(nonExistingId)).thenReturn(false);
    Mockito.when(productRepositoryMock.existsById(dependentId)).thenReturn(true);

    Mockito.when(productRepositoryMock.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
    Mockito.when(productRepositoryMock.findById(existingId)).thenReturn(Optional.of(product));
    Mockito.when(productRepositoryMock.findById(nonExistingId)).thenReturn(Optional.empty());

    Mockito.when(productRepositoryMock.save(ArgumentMatchers.any())).thenReturn(product);
    Mockito.doNothing().when(productRepositoryMock).deleteById(existingId);
    Mockito.doThrow(DataIntegrityViolationException.class).when(productRepositoryMock).deleteById(dependentId);

    // Category
    Mockito.when(categoryRepositoryMock.getReferenceById(categoryExistingId)).thenReturn(category);
  }

  @Test
  public void findAllPagedShouldReturnPage() {

    Pageable pageable = PageRequest.of(0, 10);
    Page<ProductDTO> result = service.findAllPaged("0", "", pageable);

    Assertions.assertNotNull(result);

    Mockito.verify(productRepositoryMock, Mockito.times(1)).findAll(pageable);

  }

  @Test
  public void findByIdShouldReturnProductDTOWhenIdExists() {

    ProductDTO result = service.findById(existingId);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(existingId, result.id());
    Mockito.verify(productRepositoryMock).findById(existingId);

  }

  @Test
  public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      service.findById(nonExistingId);
    });

  }

  @Test
  public void updateShouldReturnProductDTOWhenIdExists() {

    ProductDTO result = service.update(existingId, productDTO);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(existingId, result.id());
    Mockito.verify(productRepositoryMock).findById(existingId);
    Mockito.verify(productRepositoryMock).save(ArgumentMatchers.any());
    Mockito.verify(categoryRepositoryMock).getReferenceById(categoryExistingId);

  }

  @Test
  public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      service.update(nonExistingId, productDTO);
    });

  }

  @Test
  public void deleteShouldThrowDatabaseExceptionWhenDependentId() {

    Assertions.assertThrows(DatabaseException.class, () -> {
      service.delete(dependentId);
    });

  }

  @Test
  public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      service.delete(nonExistingId);
    });

  }

  @Test
  public void deleteShouldDoNothingWhenIdExists() {
    Assertions.assertDoesNotThrow(() -> {
      service.delete(existingId);
    });

    Mockito.verify(productRepositoryMock, Mockito.times(1)).deleteById(existingId);
  }

}
