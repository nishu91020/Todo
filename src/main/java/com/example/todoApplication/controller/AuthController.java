package com.example.todoApplication.controller;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.todoApplication.LambdaHandler;
import com.example.todoApplication.model.User;
import com.example.todoApplication.service.AuthService;
import com.example.todoApplication.utility.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.amazonaws.services.lambda.runtime.Context;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @Autowired
    JwtUtil jwtUtil;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    public AuthController() {
       this.authService = new AuthService();
    }

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        String path = input.getPath();
        String method = input.getHttpMethod();
        String body = input.getBody();

        logger.info("AuthController received path: {} method: {}", path, method);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        try {
            ObjectMapper mapper = new ObjectMapper();
            if ("/LambdaHandler/auth/signup".equals(path) && "POST".equalsIgnoreCase(method)) {
                logger.info("Signing up user with body: {}", body);
                User user = mapper.readValue(body, User.class);
                String result = authService.signup(user);
                return new APIGatewayProxyResponseEvent()
                        .withHeaders(headers)
                        .withStatusCode(201)
                        .withBody(result);
            } else if ("/LambdaHandler/auth/login".equals(path) && "POST".equalsIgnoreCase(method)) {
                logger.info("Logging in user with body: {}", body);
                User user = mapper.readValue(body, User.class);
                String result = authService.validateUser(user.getUsername(), user.getPassword());
                return new APIGatewayProxyResponseEvent()
                        .withHeaders(headers)
                        .withStatusCode(200)
                        .withBody(result);
            } else if ("/LambdaHandler/auth/test".equals(path) && "GET".equalsIgnoreCase(method)) {
                logger.info("Testing integration");
                return new APIGatewayProxyResponseEvent()
                        .withHeaders(headers)
                        .withStatusCode(200)
                        .withBody("successful integration");
            } else {
                return new APIGatewayProxyResponseEvent()
                        .withHeaders(headers)
                        .withStatusCode(404)
                        .withBody("{\"error\":\"Not Found\"}");
            }
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(headers)
                    .withStatusCode(500)
                    .withBody("{\"error\":\"Internal Server Error\"}");
        }
    }

    @PostMapping("/signup")
    private ResponseEntity<String> signupUser(@RequestBody User user){ //signup
        System.out.println("Signing up user!!");
        return new ResponseEntity<>(authService.signup(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    private ResponseEntity<String> loginUser(@RequestBody User user){
        System.out.println("Logging in user!!");
        return new ResponseEntity<>(authService.validateUser(user.getUsername(), user.getPassword()),HttpStatus.FOUND);
    }

    @GetMapping("/test")
    private ResponseEntity<String> test(){
        return new ResponseEntity<>("successful integration",HttpStatus.FOUND);
    }
}
