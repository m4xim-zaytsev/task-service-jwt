package com.example.task_service_jwt.service;

import com.example.task_service_jwt.entity.Task;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TaskService {
    Task createTask(Task task, UserDetails userDetails);
    Task getById(Long id);
    List<Task> findAll();
    void delete(Long id, UserDetails userDetails);
    Task update(Long id, Task task, UserDetails userDetails);
}
