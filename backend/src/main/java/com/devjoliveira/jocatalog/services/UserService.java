package com.devjoliveira.jocatalog.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocatalog.dtos.UserDTO;
import com.devjoliveira.jocatalog.dtos.UserMinDTO;
import com.devjoliveira.jocatalog.entities.User;
import com.devjoliveira.jocatalog.repositories.RoleRepository;
import com.devjoliveira.jocatalog.repositories.UserRepository;
import com.devjoliveira.jocatalog.services.exceptions.DatabaseException;
import com.devjoliveira.jocatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  public UserService(UserRepository UserRepository, RoleRepository roleRepository) {
    this.userRepository = UserRepository;
    this.roleRepository = roleRepository;
  }

  @Transactional(readOnly = true)
  public Page<UserDTO> findAllPaged(Pageable pageable) {
    return userRepository.findAll(pageable).map(UserDTO::new);
  }

  @Transactional(readOnly = true)
  public UserDTO findById(Long id) {
    return userRepository.findById(id)
        .map(entity -> new UserDTO(entity))
        .orElseThrow(() -> new ResourceNotFoundException("Resource not found."));
  }

  @Transactional
  public UserMinDTO save(UserDTO dto) {
    User entity = new User();
    copyDtoToEntity(dto, entity);
    entity = userRepository.save(entity);
    return new UserMinDTO(entity);
  }

  @Transactional
  public UserMinDTO update(Long id, UserDTO dto) {
    var entity = userRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Resource Not Found."));

    copyDtoToEntity(dto, entity);

    userRepository.save(entity);

    return new UserMinDTO(entity);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public void delete(Long id) {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("Resource Not Found.");
    }

    try {
      userRepository.deleteById(id);

    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Fail in reference integrity");

    }
  }

  private void copyDtoToEntity(UserDTO dto, User entity) {

    entity.setFirstName(dto.firstName());
    entity.setLastName(dto.lastName());
    entity.setEmail(dto.email());

    // Resolve the password
    entity.setPassword(null);

    entity.getRoles().clear();

    if (dto.roles() != null) {

      dto.roles().forEach(roleDTO -> {
        var role = roleRepository.getReferenceById(roleDTO.id());
        entity.getRoles().add(role);
      });

    }

  }

}
