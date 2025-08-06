package com.devjoliveira.jocatalog.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocatalog.dtos.CategoryDTO;
import com.devjoliveira.jocatalog.entities.Category;
import com.devjoliveira.jocatalog.repositories.CategoryRepository;
import com.devjoliveira.jocatalog.services.exceptions.DatabaseException;
import com.devjoliveira.jocatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Transactional(readOnly = true)
  public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
    return categoryRepository.findAll(pageRequest).map(CategoryDTO::new);
  }

  @Transactional(readOnly = true)
  public CategoryDTO findById(Long id) {
    return categoryRepository.findById(id)
        .map(CategoryDTO::new)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
  }

  @Transactional
  public CategoryDTO save(CategoryDTO categoryDTO) {
    return new CategoryDTO(
        categoryRepository.save(new Category(null, categoryDTO.name())));
  }

  @Transactional
  public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
    var category = categoryRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Category not found."));

    category.setName(categoryDTO.name());

    categoryRepository.save(category);

    return new CategoryDTO(category);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public void delete(Long id) {
    if (!categoryRepository.existsById(id)) {
      throw new ResourceNotFoundException("Resource not found.");
    }

    try {
      categoryRepository.deleteById(id);

    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Fail in reference integrity");

    }
  }

}
