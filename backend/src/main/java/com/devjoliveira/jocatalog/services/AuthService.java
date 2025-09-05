package com.devjoliveira.jocatalog.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.devjoliveira.jocatalog.dtos.EmailDTO;
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

  private final PasswordRecoverRepository recoverRepository;

  private final EmailService emailService;

  public AuthService(UserRepository userRepository, PasswordRecoverRepository recoverRepository,
      EmailService emailService) {
    this.userRepository = userRepository;
    this.recoverRepository = recoverRepository;
    this.emailService = emailService;
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

    recover = recoverRepository.save(recover);

    String textBody = "Acesse o link para definir uma nova senha: \n\n"
        + recoverUri + token + ". validade de " + tokenMinutes + " Minutos";

    emailService.sendEmail(body.email(), "RECUPERAÇÃO DE SENHA", textBody);

  }

}
