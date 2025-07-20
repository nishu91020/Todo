package com.example.todoApplication;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.todoApplication.controller.AuthController;
import com.example.todoApplication.controller.TaskController;
import com.example.todoApplication.model.User;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.todoApplication.service.AuthService;


public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LambdaHandler.class);

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        String path = input.getPath();
        String method = input.getHttpMethod();
        String body = input.getBody() != null ? input.getBody() : "";

        logger.info("received request for {} method {} body {}", path, method, body);

        if (path.startsWith("/LambdaHandler/auth")) {
            AuthController authHandler = new AuthController();
            return authHandler.handleRequest(input, context);
        } else {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(headers)
                    .withStatusCode(404)
                    .withBody("{\"error\":\"Not Found\"}");
        }
        


    }
}
