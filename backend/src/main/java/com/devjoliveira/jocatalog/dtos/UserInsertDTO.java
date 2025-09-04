package com.devjoliveira.jocatalog.dtos;

import com.devjoliveira.jocatalog.services.validation.UserInsertValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@UserInsertValid
public class UserInsertDTO extends UserDTO {

  @NotBlank(message = "Password is Required")
  // @Size(min = 8, message = "Password must be at least 8 characters")
  @Pattern(message = "Mínimo de oito caracteres, uma letra maiúscula, uma letra minúscula, um número e um caractere especial", regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
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
