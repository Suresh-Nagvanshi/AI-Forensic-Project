package com.example.aiforensics.service.auth;

import com.example.aiforensics.dto.SignupRequest;
import com.example.aiforensics.entity.User;
import com.example.aiforensics.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(SignupRequest request) {
        System.out.println("registerUser reached for email = " + request.getEmail());
        System.out.println("existsByEmail = " + userRepository.existsByEmail(request.getEmail()));
        System.out.println("password matches = " + request.getPassword().equals(request.getConfirmPassword()));

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");

        System.out.println("Saving user now...");
        userRepository.save(user);
        System.out.println("User saved successfully with email = " + user.getEmail());
    }
}