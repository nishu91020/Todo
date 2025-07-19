package com.example.todoApplication.controller;

import com.example.todoApplication.model.User;
import com.example.todoApplication.service.AuthService;
import com.example.todoApplication.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/signup")
    private ResponseEntity<String> signupUser(@RequestBody User user){ //signup
        System.out.println("Signing up user!!");
        return new ResponseEntity<>(authService.signup(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    private ResponseEntity<String> loginUser(@RequestBody User user){
        System.out.println("Sign in user!!");
        return new ResponseEntity<>(authService.validateUser(user.getUsername(), user.getPassword()),HttpStatus.FOUND);
    }

    @GetMapping("/test")
    private ResponseEntity<String> test(){
        return new ResponseEntity<>("successful integration",HttpStatus.FOUND);
    }
}
