package com.example.task_service_jwt.service;

import com.example.task_service_jwt.entity.Review;
import com.example.task_service_jwt.entity.Task;
import com.example.task_service_jwt.entity.TaskStatus;
import com.example.task_service_jwt.web.filter.TaskFilter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TaskService {
    Task createTask(Task task, UserDetails userDetails);
    Task findById(Long id);
    List<Task> findByAuthor(Long authorId);
    List<Task> findByExecutor(Long executorId);
    Task updateStatus(TaskStatus status, Long taskId, UserDetails userDetails);
    List<Task> findAll();
    void delete(Long id, UserDetails userDetails);
    Task update(Long id, Task task, UserDetails userDetails);

    List<Task> findByFilter(TaskFilter taskFilter);
}
