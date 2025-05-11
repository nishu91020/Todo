package com.example.todoApplication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    String id;
    String username;
    String password;
}
