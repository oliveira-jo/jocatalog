package com.devjoliveira.jocatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devjoliveira.jocatalog.entities.PasswordRecover;

public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long> {

}
