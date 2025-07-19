package com.example.todoApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class TodoFunctionConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(TodoFunctionConfiguration.class);

    private final TodoService todoService;
    private final WebClient webClient;

    public TodoFunctionConfiguration(TodoService todoService, WebClient webClient) {
        this.todoService = todoService;
        this.webClient = webClient;
    }

    @Bean
    public TodoService todoService() {
        return new TodoService(); // Replace with your actual service
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(java.time.Duration.ofSeconds(5))
                        .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> todoRoutes() {
        return route()
                .GET("/todos", request -> {
                    logger.info("Handling GET /todos");
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "hello world");
                    response.put("todos", todoService.getAllTodos());
                    return ok().contentType(MediaType.APPLICATION_JSON)
                            .body(Mono.just(response), Map.class);
                })
                .POST("/todos", request -> request.bodyToMono(Todo.class)
                        .flatMap(todo -> {
                            logger.info("Handling POST /todos with todo: {}", todo);
                            Todo created = todoService.createTodo(todo);
                            Map<String, Object> response = Map.of(
                                    "message", "Todo created",
                                    "body", created
                            );
                            return ServerResponse.created(URI.create("/todos/" + todo.getId()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(Mono.just(response), Map.class);
                        }))
                .GET("/todos/{id}", request -> {
                    String id = request.pathVariable("id");
                    logger.info("Handling GET /todos/{}", id);
                    Todo todo = todoService.getTodoById(id);
                    if (todo == null) {
                        return ServerResponse.notFound().build();
                    }
                    Map<String, Object> response = Map.of(
                            "message", "Todo with ID " + id,
                            "id", id,
                            "todo", todo
                    );
                    return ok().contentType(MediaType.APPLICATION_JSON)
                            .body(Mono.just(response), Map.class);
                })
                .DELETE("/todos/{id}", request -> {
                    String id = request.pathVariable("id");
                    logger.info("Handling DELETE /todos/{}", id);
                    boolean deleted = todoService.deleteTodo(id);
                    if (!deleted) {
                        return ServerResponse.notFound().build();
                    }
                    Map<String, String> response = Map.of(
                            "message", "Todo with ID " + id + " deleted"
                    );
                    return ok().contentType(MediaType.APPLICATION_JSON)
                            .body(Mono.just(response), Map.class);
                })
                .build();
    }
}