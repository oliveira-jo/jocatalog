package com.devjoliveira.jocatalog.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDTO(
        @NotBlank(message = "Email is required") @Email(message = "Invalid email") String email) {

}
