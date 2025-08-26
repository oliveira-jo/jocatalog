package com.devjoliveira.jocatalog.dtos;

import com.devjoliveira.jocatalog.entities.Role;

public record RoleDTO(
    Long id,
    String authority) {

  public RoleDTO(Role role) {
    this(role.getId(), role.getAuthority());
  }

}
