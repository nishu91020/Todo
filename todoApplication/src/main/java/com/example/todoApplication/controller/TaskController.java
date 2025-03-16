package com.example.todoApplication.controller;

import com.example.todoApplication.model.Task;
import com.example.todoApplication.service.TaskService;
import com.example.todoApplication.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasksForUser(@RequestHeader String token) {
        String jwtToken = token.replace("Bearer ","");
        String username = jwtUtil.extractUsername(jwtToken);

        // TODO: update logic to check the username and validity in one go
        if(!jwtUtil.isTokenValid(jwtToken, username)){
            return new ResponseEntity<>(new ArrayList<Task>() {}, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(taskService.getAllTasks(username), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id, @RequestHeader String token) {

        Task task = taskService.getTaskById(id);
        if (task != null) {
            if(jwtUtil.isTokenValid(token, task.getUsername()))
            {
                return new ResponseEntity<>(task, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody Task task, @RequestHeader String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if(jwtUtil.isTokenValid(token, task.getUsername()))
        {
            return new ResponseEntity<>(taskService.createTask(task, token), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody Task task, @RequestHeader String token) {
        String jwtToken = token.replace("Bearer ","");
        System.out.println(jwtToken);
        System.out.println(task.getUsername());
        System.out.println("task to updated" +task.isCompleted());
        if(!jwtUtil.isTokenValid(jwtToken, task.getUsername()))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Task updatedTask = taskService.updateTask(id, task);
        if (updatedTask != null) {
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id, @RequestHeader String token) {
        String jwtToken = token.replace("Bearer ","");
        if(!jwtUtil.isTokenValid(jwtToken, jwtUtil.extractUsername(jwtToken)))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
