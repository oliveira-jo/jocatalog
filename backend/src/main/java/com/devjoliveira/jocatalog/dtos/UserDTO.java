package com.devjoliveira.jocatalog.dtos;

import java.util.Set;
import java.util.stream.Collectors;

import com.devjoliveira.jocatalog.entities.User;
import com.devjoliveira.jocatalog.services.validation.UserInsertValid;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@UserInsertValid
public record UserDTO(

    Long id,
    @NotBlank(message = "FirstName is required") String firstName,
    @NotBlank(message = "LastName is required") String lastName,
    @Email(message = "Email is Required") String email,
    @NotBlank(message = "Password is Required") String password,
    Set<RoleDTO> roles) {

  public UserDTO(User user) {
    this(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail(),
        user.getPassword(),
        user.getRoles().stream().map(
            role -> new RoleDTO(role.getId(), role.getAuthority()))
            .collect(Collectors.toSet()));
  }

}
