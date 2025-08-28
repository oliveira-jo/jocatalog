package com.devjoliveira.jocatalog.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.devjoliveira.jocatalog.controllers.exceptions.FieldMessage;
import com.devjoliveira.jocatalog.dtos.UserUpdateDTO;
import com.devjoliveira.jocatalog.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private UserRepository repository;

  @Override
  public void initialize(UserUpdateValid ann) {
  }

  @Override
  public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

    @SuppressWarnings("unchecked")
    var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    long userId = Long.parseLong(uriVars.get("id").toString());

    List<FieldMessage> list = new ArrayList<>();

    if (repository.findByEmail(dto.getEmail()).isPresent()
        && repository.findByEmail(dto.getEmail()).get().getId() != userId) {
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