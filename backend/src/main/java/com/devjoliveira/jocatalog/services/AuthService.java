package com.devjoliveira.jocatalog.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocatalog.dtos.EmailDTO;
import com.devjoliveira.jocatalog.dtos.NewPasswordDTO;
import com.devjoliveira.jocatalog.entities.PasswordRecover;
import com.devjoliveira.jocatalog.entities.User;
import com.devjoliveira.jocatalog.repositories.PasswordRecoverRepository;
import com.devjoliveira.jocatalog.repositories.UserRepository;
import com.devjoliveira.jocatalog.services.exceptions.ResourceNotFoundException;

@Service
public class AuthService {

  @Value("${email.password-recover.token.minutes}")
  private Long tokenMinutes;

  @Value("${email.password-recover.uri}")
  private String recoverUri;

  private final UserRepository userRepository;

  private final PasswordRecoverRepository passwordRecoverRepository;

  private final EmailService emailService;

  private final PasswordEncoder passwordEncoder;

  public AuthService(UserRepository userRepository, PasswordRecoverRepository passwordRecoverRepository,
      EmailService emailService, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordRecoverRepository = passwordRecoverRepository;
    this.emailService = emailService;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public void createRecoverToken(EmailDTO body) {

    Optional<User> user = userRepository.findByEmail(body.email());

    if (user.isEmpty()) {
      throw new ResourceNotFoundException("Mail not found");
    }

    String token = UUID.randomUUID().toString();

    PasswordRecover recover = new PasswordRecover(
        null,
        token,
        body.email(),
        Instant.now().plusSeconds(tokenMinutes * 60L));

    recover = passwordRecoverRepository.save(recover);

    String textBody = "Acesse o link para definir uma nova senha: \n\n"
        + recoverUri + token + ". validade de " + tokenMinutes + " Minutos";

    emailService.sendEmail(body.email(), "RECUPERAÇÃO DE SENHA", textBody);

  }

  @Transactional
  public void saveNewPassword(NewPasswordDTO body) {

    List<PasswordRecover> response = passwordRecoverRepository.searchValidTokens(body.token(), Instant.now());

    if (response.size() == 0) {
      throw new ResourceNotFoundException("Invalid token");
    }

    User user = userRepository.findByEmail(response.get(0).getEmail()).get();
    user.setPassword(passwordEncoder.encode(body.password()));

    user = userRepository.save(user);

  }

}
