package com.devjoliveira.jocatalog.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocatalog.dtos.CategoryDTO;
import com.devjoliveira.jocatalog.entities.Category;
import com.devjoliveira.jocatalog.repositories.CategoryRepository;

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
        .orElseThrow(() -> new RuntimeException("Object not found"));
  }

  @Transactional
  public CategoryDTO save(CategoryDTO categoryDTO) {
    return new CategoryDTO(
        categoryRepository.save(new Category(null, categoryDTO.name())));
  }

  @Transactional
  public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
    var category = categoryRepository.getReferenceById(id);

    if (category == null) {
      throw new RuntimeException("Object not found");
    }

    category.setName(categoryDTO.name());
    var newCat = categoryRepository.save(category);

    return new CategoryDTO(newCat);
  }

  @Transactional
  public void delete(Long id) {
    if (!categoryRepository.existsById(id)) {
      throw new RuntimeException("Object not found");
    }
    categoryRepository.deleteById(id);
  }

}
