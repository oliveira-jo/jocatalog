package com.devjoliveira.jocatalog.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocatalog.dtos.ProductDTO;
import com.devjoliveira.jocatalog.entities.Product;
import com.devjoliveira.jocatalog.repositories.CategoryRepository;
import com.devjoliveira.jocatalog.repositories.ProductRepository;
import com.devjoliveira.jocatalog.services.exceptions.DatabaseException;
import com.devjoliveira.jocatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }

  @Transactional(readOnly = true)
  public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
    return productRepository.findAll(pageRequest).map(ProductDTO::new);
  }

  @Transactional(readOnly = true)
  public ProductDTO findById(Long id) {
    return productRepository.findById(id)
        .map(x -> new ProductDTO(x, x.getCategories()))
        .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
  }

  @Transactional
  public ProductDTO save(ProductDTO productDTO) {
    Product product = new Product();
    copyDtoToEntity(productDTO, product);
    product = productRepository.save(product);
    return new ProductDTO(product, product.getCategories());
  }

  @Transactional
  public ProductDTO update(Long id, ProductDTO productDTO) {
    var entity = productRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Product not found."));

    copyDtoToEntity(productDTO, entity);

    productRepository.save(entity);

    return new ProductDTO(entity);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public void delete(Long id) {
    if (!productRepository.existsById(id)) {
      throw new ResourceNotFoundException("Product not found.");
    }

    try {
      productRepository.deleteById(id);

    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Fail in reference integrity");

    }
  }

  private void copyDtoToEntity(ProductDTO productDTO, Product entity) {
    entity.setName(productDTO.name());
    entity.setDescription(productDTO.description());
    entity.setPrice(productDTO.price());
    entity.setImgUrl(productDTO.imgUrl());
    entity.setDate(productDTO.date());

    entity.getCategories().clear();

    if (productDTO.categories() != null) {
      productDTO.categories().forEach(categoryDTO -> {
        var category = categoryRepository.getReferenceById(categoryDTO.id());
        entity.getCategories().add(category);
      });
    }

  }

}
