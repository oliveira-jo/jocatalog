package com.devjoliveira.jocatalog.dtos;

import com.devjoliveira.jocatalog.services.validation.UserInsertValid;

import jakarta.validation.constraints.NotBlank;

@UserInsertValid
public class UserInsertDTO extends UserDTO {

  @NotBlank(message = "Password is Required")
  private String password;

  public UserInsertDTO() {
    super();
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

}
