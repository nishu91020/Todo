package com.example.todoApplication.service;

import com.example.todoApplication.model.User;
import com.example.todoApplication.repository.AuthRepository;
import com.example.todoApplication.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;

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
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        authRepository.save(user);
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
