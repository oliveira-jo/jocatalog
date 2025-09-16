package com.devjoliveira.jocatalog.services;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocatalog.dtos.ProductDTO;
import com.devjoliveira.jocatalog.entities.Product;
import com.devjoliveira.jocatalog.projections.ProductProjection;
import com.devjoliveira.jocatalog.repositories.CategoryRepository;
import com.devjoliveira.jocatalog.repositories.ProductRepository;
import com.devjoliveira.jocatalog.services.exceptions.DatabaseException;
import com.devjoliveira.jocatalog.services.exceptions.ResourceNotFoundException;
import com.devjoliveira.jocatalog.utils.Util;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }

  /**
   * This method implements a search with filter by name and categories
   * and resolve the n + 1 problem.
   */
  @SuppressWarnings("unchecked")
  @Transactional(readOnly = true)
  public Page<ProductDTO> findAllPaged(String categoryId, String name, Pageable pageable) {

    List<Long> categoryIds = Arrays.asList();

    if (!categoryId.equals("0")) {
      categoryIds = Arrays.asList(categoryId.split(",")).stream().map(Long::parseLong).toList();
    }

    // aux search for get on hand products ids
    Page<ProductProjection> page = productRepository.searchProducts(categoryIds, name.trim(), pageable);

    // get products ids
    List<Long> productIds = page.map(x -> x.getId()).toList();

    // get products searching for products ids - ** desordered result **
    List<Product> products = productRepository.searchProductsWithCategories(productIds);

    // replace or ORDER a page ordered put this order to (products desordered)
    products = (List<Product>) Util.replace(page.getContent(), products);

    // transform to DTOs
    List<ProductDTO> dtos = products.stream().map(p -> new ProductDTO(p, p.getCategories())).toList();

    // create page implementation from DTOs
    return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());

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

    product.setDate(Instant.now());

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
