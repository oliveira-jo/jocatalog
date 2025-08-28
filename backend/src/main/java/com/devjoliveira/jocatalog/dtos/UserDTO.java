package com.devjoliveira.jocatalog.dtos;

import java.util.Set;
import java.util.stream.Collectors;

import com.devjoliveira.jocatalog.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {

  private Long id;
  @NotBlank(message = "FirstName is required")
  private String firstName;
  @NotBlank(message = "LastName is required")
  private String lastName;
  @Email(message = "Email is Required")
  private String email;

  private Set<RoleDTO> roles;

  public UserDTO() {
  }

  public UserDTO(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.email = user.getEmail();
    this.roles = user.getRoles().stream().map(roles -> new RoleDTO(roles.getId(), roles.getAuthority()))
        .collect(Collectors.toSet());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<RoleDTO> getRoles() {
    return roles;
  }

  public void setRoles(Set<RoleDTO> roles) {
    this.roles = roles;
  }

}
