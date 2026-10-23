package dev.hari.userauthservice.service;

import dev.hari.userauthservice.dto.AuthRequest;
import dev.hari.userauthservice.model.Role;
import dev.hari.userauthservice.model.User;
import dev.hari.userauthservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User signUp(String email, String password){
        if(userRepository.existsByEmail(email)) return null;

        User user = new User();

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);

        return userRepository.save(user);


    }

}
