package dev.java.AuthAPI.service;

import dev.java.AuthAPI.domain.User;
import dev.java.AuthAPI.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        log.info("Tentando registrar usuário com email: {}", user.getEmail());

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.warn("Tentativa de cadastro falhou: Email {} já existe", user.getEmail());
            throw new RuntimeException("Este email já está cadastrado!");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        log.info("Usuário registrado com sucesso");
        return userRepository.save(user);
    }
}
