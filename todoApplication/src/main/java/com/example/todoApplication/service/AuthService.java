package com.example.todoApplication.service;

import com.example.todoApplication.model.Task;
import com.example.todoApplication.model.User;
import com.example.todoApplication.repository.AuthRepository;
import com.example.todoApplication.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    AuthRepository authRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String signup(User user){
        if (authRepository.findByUsername(user.getUsername()).isPresent()) {
            return "User already exists!";
        }
        User newUser = new User();
        UUID randomUUID = UUID.randomUUID();

        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setId(randomUUID.toString());
        authRepository.save(newUser);
        return "User registered successfully!";
    }
    public String validateUser(@RequestParam String username, @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return "Bearer " + token;
    }
}
