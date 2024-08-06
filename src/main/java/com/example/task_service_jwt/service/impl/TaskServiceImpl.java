package com.example.task_service_jwt.service.impl;

import com.example.task_service_jwt.entity.Task;
import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.exception.EntityNotFoundException;
import com.example.task_service_jwt.exception.IllegalAccessException;
import com.example.task_service_jwt.repository.TaskRepository;
import com.example.task_service_jwt.repository.UserRepository;
import com.example.task_service_jwt.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Task createTask(Task task, UserDetails userDetails) {
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        Optional<User> optionalExecutor = userRepository.findByUsername(task.getExecutor().getUsername());

        User author = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found: " + userDetails.getUsername()));
        User executor = optionalExecutor.orElseThrow(() -> new UsernameNotFoundException("Executor not found"));

        task.setAuthor(author);
        task.setExecutor(executor);
        return taskRepository.save(task);
    }

    @Override
    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(MessageFormat.format("Task with id: {0} not found",id))
        );
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public void delete(Long id, UserDetails userDetails) {
        Task taskToDelete = getById(id);
        if(userDetails.getUsername().equals(taskToDelete.getAuthor().getUsername()))
            taskRepository.deleteById(id);
    }

    @Override
    public Task update(Long id, Task task, UserDetails userDetails) {
        Task taskToUpdate = getById(id);

        if(!taskToUpdate.getAuthor().getUsername().equals(userDetails.getUsername()))
            throw new IllegalAccessException("only author can update task");

        taskToUpdate.setName(task.getName());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setTaskPriority(task.getTaskPriority());
        if(task.getExecutor()!=null)
            taskToUpdate.setExecutor(task.getExecutor());

        return taskRepository.save(taskToUpdate);
    }
}
