package com.example.todoApplication.config;

import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.todoApplication.repository")
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(MongoClients.create("mongodb+srv://rainishu111:Nishu4044054@cluster0.23ebv.mongodb.net/todo_db?retryWrites=true&w=majority&appName=Cluster0"), "todo_db");
    }
}
