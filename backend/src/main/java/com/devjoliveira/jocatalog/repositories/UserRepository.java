package com.devjoliveira.jocatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devjoliveira.jocatalog.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
