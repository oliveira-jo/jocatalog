package com.devjoliveira.jocatalog.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocatalog.dtos.ProductDTO;
import com.devjoliveira.jocatalog.entities.Product;
import com.devjoliveira.jocatalog.repositories.ProductRepository;
import com.devjoliveira.jocatalog.services.exceptions.DatabaseException;
import com.devjoliveira.jocatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
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
    return new ProductDTO(
        productRepository.save(
            new Product(null, productDTO.name(), productDTO.description(), productDTO.price(), productDTO.imgUrl(),
                productDTO.date())));
  }

  @Transactional
  public ProductDTO update(Long id, ProductDTO productDTO) {
    var entity = productRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Product not found."));

    entity.setName(productDTO.name());
    entity.setDescription(productDTO.description());
    entity.setPrice(productDTO.price());
    entity.setImgUrl(productDTO.imgUrl());

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

}
