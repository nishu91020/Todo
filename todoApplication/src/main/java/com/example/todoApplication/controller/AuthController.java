package com.example.todoApplication.controller;

import com.example.todoApplication.model.User;
import com.example.todoApplication.service.AuthService;
import com.example.todoApplication.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/signup")
    private ResponseEntity<String> signupUser(User user){
        return new ResponseEntity<>(authService.signup(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    private ResponseEntity<String> loginUser(User user){
        return new ResponseEntity<>(authService.validateUser(user.getUsername(), user.getPassword()),HttpStatus.FOUND);
    }
}
