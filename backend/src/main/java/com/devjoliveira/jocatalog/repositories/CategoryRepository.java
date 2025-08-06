package com.devjoliveira.jocatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devjoliveira.jocatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
