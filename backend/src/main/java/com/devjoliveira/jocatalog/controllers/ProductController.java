package com.devjoliveira.jocatalog.controllers;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devjoliveira.jocatalog.dtos.ProductDTO;
import com.devjoliveira.jocatalog.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
    return ResponseEntity.ok().body(productService.findAllPaged(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok().body(productService.findById(id));
  }

  @PostMapping
  public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO productDTO) {
    productDTO = productService.save(productDTO);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(productDTO.id()).toUri();

    return ResponseEntity.created(uri).body(productDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDTO> change(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
    return ResponseEntity.ok().body(productService.update(id, productDTO));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
