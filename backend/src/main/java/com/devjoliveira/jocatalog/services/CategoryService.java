package com.devjoliveira.jocatalog.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocatalog.dtos.CategoryDTO;
import com.devjoliveira.jocatalog.entities.Category;
import com.devjoliveira.jocatalog.repositories.CategoryRepository;
import com.devjoliveira.jocatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Transactional(readOnly = true)
  public List<CategoryDTO> findAll() {
    return categoryRepository.findAll().stream().map(CategoryDTO::new).toList();
  }

  @Transactional(readOnly = true)
  public CategoryDTO findById(Long id) {
    return categoryRepository.findById(id)
        .map(CategoryDTO::new)
        .orElseThrow(() -> new EntityNotFoundException("Category not found."));
  }

  @Transactional
  public CategoryDTO save(CategoryDTO categoryDTO) {
    return new CategoryDTO(
        categoryRepository.save(new Category(null, categoryDTO.name())));
  }

  @Transactional
  public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
    var category = categoryRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Category not found."));

    category.setName(categoryDTO.name());

    categoryRepository.save(category);

    return new CategoryDTO(category);
  }

  @Transactional
  public void delete(Long id) {
    var category = categoryRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Category not found."));

    categoryRepository.deleteById(category.getId());
  }

}
