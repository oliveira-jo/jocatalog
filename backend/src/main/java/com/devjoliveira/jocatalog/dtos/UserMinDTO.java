package com.devjoliveira.jocatalog.dtos;

import java.util.Set;
import java.util.stream.Collectors;

import com.devjoliveira.jocatalog.entities.User;

public record UserMinDTO(

                Long id,
                String firstName,
                String lastName,
                String email,
                Set<RoleDTO> roles) {

        public UserMinDTO(User user) {
                this(
                                user.getId(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getEmail(),
                                user.getRoles().stream().map(
                                                role -> new RoleDTO(role.getId(), role.getAuthority()))
                                                .collect(Collectors.toSet()));
        }

}
