package com.example.todoApplication.service;

import com.example.todoApplication.model.Task;
import com.example.todoApplication.repository.TaskRepository;
import com.example.todoApplication.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    public List<Task> getAllTasks(String username) {
        return taskRepository.findByUsername(username);
    }

    public Task getTaskById(String id) {
        return taskRepository.findById(id).orElse(null);
    }

    public String createTask(Task task, String token) {
        taskRepository.save(task);
        return "Task Created successfully";
    }

    public Task updateTask(String id, Task updatedTask) {
        Optional<Task> existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            Task task = existingTask.get();
            task.setName(updatedTask.getName());
            task.setDescription(updatedTask.getDescription());
            task.setDueDate(updatedTask.getDueDate());
            task.setCompleted(updatedTask.isCompleted());
            return taskRepository.save(task);
        }
        return null;
    }

    public ResponseEntity<Void> deleteTask(String id) {

        taskRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
