package dev.java.AuthAPI.controller;

import dev.java.AuthAPI.domain.User;
import dev.java.AuthAPI.dto.LoginRequest;
import dev.java.AuthAPI.dto.RegisterRequest;
import dev.java.AuthAPI.dto.ResponseDTO;
import dev.java.AuthAPI.repository.UserRepository;
import dev.java.AuthAPI.service.TokenService;
import dev.java.AuthAPI.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequest request) {
        User newUser = new User();
        newUser.setName(request.name());
        newUser.setEmail(request.email());
        newUser.setPassword(request.password());

        userService.registerUser(newUser);

        return ResponseEntity.ok("Usuário registrdo com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request) {
        User user = this.userRepository.findByEmail(request.email()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (passwordEncoder.matches(request.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
