package com.devjoliveira.jocatalog.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devjoliveira.jocatalog.entities.Role;
import com.devjoliveira.jocatalog.entities.User;
import com.devjoliveira.jocatalog.projections.UserDetailsProjection;
import com.devjoliveira.jocatalog.repositories.UserRepository;

@Service
public class AuthService implements UserDetailsService {

  private final UserRepository userRepository;

  public AuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
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

}
