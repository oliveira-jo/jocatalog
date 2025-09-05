package com.devjoliveira.jocatalog.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record NewPasswordDTO(
    @NotBlank(message = "Password is Required") String token,
    @NotBlank(message = "Password is Required") @Pattern(message = "Mínimo de oito caracteres, uma letra maiúscula, uma letra minúscula, um número e um caractere especial", regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$") String password) {

}
