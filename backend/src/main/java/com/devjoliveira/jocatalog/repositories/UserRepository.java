package com.devjoliveira.jocatalog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devjoliveira.jocatalog.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

}
