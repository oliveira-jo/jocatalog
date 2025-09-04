package com.devjoliveira.jocatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devjoliveira.jocatalog.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Role findByAuthority(String authority);

}
