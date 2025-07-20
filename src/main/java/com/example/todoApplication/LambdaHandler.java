package com.example.todoApplication;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.todoApplication.model.User;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.todoApplication.service.AuthService;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LambdaHandler.class);

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        String path = input.getPath();
        String method = input.getHttpMethod();
        User body = null;
        try {
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            body = objectMapper.readValue(input.getBody(), User.class);
        } catch (Exception e) {
            logger.error("Failed to parse request body to User", e);
            return response.withStatusCode(400).withBody("{\"error\":\"Invalid request body\"}");
        }
        String queryString = input.getQueryStringParameters() != null ? input.getQueryStringParameters().toString() : "";
        String pathParameters = input.getPathParameters() != null ? input.getPathParameters().toString() : "";

        logger.info("received request for {} method {} body {}", path, method, body);
        
        AuthService authService = new AuthService();
        if(path.contains("/auth/signup")) {
            return response.withBody(authService.signup(body))
                           .withStatusCode(201);
        } else if(path.contains("/auth/login")) {
            return response.withBody(authService.validateUser(body.getUsername(), body.getPassword()))
                           .withStatusCode(201);
        } else {
            return response.withStatusCode(404).withBody("Not Found");
        }

    }
}
