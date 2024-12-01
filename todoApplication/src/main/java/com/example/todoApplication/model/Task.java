package com.example.todoApplication.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tasks")
public class Task {
    @Setter
    @Getter
    @Id
    private String id;
    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private String description;
    @Setter
    @Getter
    private LocalDateTime createdAt;
    @Setter
    @Getter
    private LocalDateTime dueDate;
    @Setter
    @Getter
    private boolean isCompleted;

    public Task(String name, String description, LocalDateTime dueDate, boolean isCompleted) {
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }
}
