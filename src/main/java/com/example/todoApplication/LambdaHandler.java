package com.example.todoApplication;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LambdaHandler.class);

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try{
            String path = input.getPath();
            String method = input.getHttpMethod();
            String body = input.getBody();
            String queryString = input.getQueryStringParameters() != null ? input.getQueryStringParameters().toString() : "";
            String pathParameters = input.getPathParameters() != null ? input.getPathParameters().toString() : "";

            if("/auth/login".equals(path))
            {
                logger.info("Login request received");

            }
        }
        catch (Exception e) {
            logger.error("Error processing request: " + e.getMessage());
            response.withStatusCode(500).withBody("{\"error\": \"Internal Server Error\"}");
        }
            String output = String.format("{ \"message\": \"hello world\"}");

            return response
                    .withStatusCode(200)
                    .withBody(output);

    }
}
