package com.devjoliveira.jocatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.devjoliveira.jocatalog.controllers.exceptions.FieldMessage;
import com.devjoliveira.jocatalog.dtos.UserDTO;
import com.devjoliveira.jocatalog.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserDTO> {

  @Autowired
  private UserRepository repository;

  @Override
  public void initialize(UserInsertValid ann) {
  }

  @Override
  public boolean isValid(UserDTO dto, ConstraintValidatorContext context) {

    List<FieldMessage> list = new ArrayList<>();

    if (repository.findByEmail(dto.email()).isPresent()) {
      list.add(new FieldMessage("email", "Email already exists"));
    }

    for (FieldMessage e : list) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(e.message()).addPropertyNode(e.message())
          .addConstraintViolation();
    }
    return list.isEmpty();
  }
}