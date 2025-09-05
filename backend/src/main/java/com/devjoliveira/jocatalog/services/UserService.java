package com.devjoliveira.jocatalog.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocatalog.dtos.UserDTO;
import com.devjoliveira.jocatalog.dtos.UserInsertDTO;
import com.devjoliveira.jocatalog.dtos.UserUpdateDTO;
import com.devjoliveira.jocatalog.entities.Role;
import com.devjoliveira.jocatalog.entities.User;
import com.devjoliveira.jocatalog.projections.UserDetailsProjection;
import com.devjoliveira.jocatalog.repositories.RoleRepository;
import com.devjoliveira.jocatalog.repositories.UserRepository;
import com.devjoliveira.jocatalog.services.exceptions.DatabaseException;
import com.devjoliveira.jocatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository UserRepository, RoleRepository roleRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = UserRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
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
  public UserDTO save(UserInsertDTO dto) {
    User entity = new User();

    copyDtoToEntity(dto, entity);

    // Every new user will have the ROLE_OPERATOR role
    entity.getRoles().clear();
    entity.getRoles().add(roleRepository.findByAuthority("ROLE_OPERATOR"));

    entity.setPassword(passwordEncoder.encode(dto.getPassword()));
    entity = userRepository.save(entity);

    return new UserDTO(entity);
  }

  @Transactional
  public UserDTO update(Long id, UserUpdateDTO dto) {
    var entity = userRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Resource Not Found."));

    copyDtoToEntity(dto, entity);

    userRepository.save(entity);

    return new UserDTO(entity);
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

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
    if (result.isEmpty()) {
      throw new UsernameNotFoundException("Email not found");
    }

    User user = new User();
    user.setEmail(username);
    user.setPassword(result.get(0).getPassword());

    result.forEach(
        role -> user.getRoles().add(new Role(role.getRoleId(), role.getAuthority())));

    return user;

  }

  private void copyDtoToEntity(UserDTO dto, User entity) {

    entity.setFirstName(dto.getFirstName());
    entity.setLastName(dto.getLastName());
    entity.setEmail(dto.getEmail());

    entity.getRoles().clear();

    if (dto.getRoles() != null) {

      dto.getRoles().forEach(roleDTO -> {
        var role = roleRepository.getReferenceById(roleDTO.id());
        entity.getRoles().add(role);
      });

    }

  }

}
