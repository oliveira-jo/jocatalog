package com.devjoliveira.jocatalog.dtos;

import java.util.Set;
import java.util.stream.Collectors;

import com.devjoliveira.jocatalog.entities.User;

public record UserDTO(

    Long id,
    String firstName,
    String lastName,
    String email,
    String password,
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
