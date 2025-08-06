package com.devjoliveira.jocatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devjoliveira.jocatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
