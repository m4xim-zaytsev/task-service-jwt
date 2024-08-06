package com.example.task_service_jwt.service.impl;

import com.example.task_service_jwt.entity.Task;
import com.example.task_service_jwt.entity.TaskStatus;
import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.exception.EntityNotFoundException;
import com.example.task_service_jwt.exception.IllegalAccessException;
import com.example.task_service_jwt.repository.TaskRepository;
import com.example.task_service_jwt.repository.TaskSpecification;
import com.example.task_service_jwt.repository.UserRepository;
import com.example.task_service_jwt.service.TaskService;
import com.example.task_service_jwt.utils.BeanUtils;
import com.example.task_service_jwt.web.filter.TaskFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(MessageFormat.format("Task with id: {0} not found",id))
        );
    }

    @Override
    public List<Task> findByAuthor(Long authorId) {
        return taskRepository.findByAuthorId(authorId);
    }

    @Override
    public List<Task> findByExecutor(Long executorId) {
        return taskRepository.findByExecutorId(executorId);
    }

    @Override
    public Task updateStatus(TaskStatus status, Long taskId, UserDetails userDetails) {
        Task task = findById(taskId);

        if(!task.getAuthor().getUsername().equals(userDetails.getUsername()))
            throw new IllegalAccessException("only author can update task");
        if(status != null)
            task.setStatus(status);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public void delete(Long id, UserDetails userDetails) {
        Task taskToDelete = findById(id);
        if(userDetails.getUsername().equals(taskToDelete.getAuthor().getUsername()))
            taskRepository.deleteById(id);
    }

    @Override
    public Task update(Long id, Task task, UserDetails userDetails) {
        Task taskToUpdate = findById(id);

        if(!taskToUpdate.getAuthor().getUsername().equals(userDetails.getUsername()))
            throw new IllegalAccessException("only author can update task");

        BeanUtils.copyProperties(taskToUpdate,task);
        if(task.getExecutor()!=null)
            taskToUpdate.setExecutor(task.getExecutor());

        return taskRepository.save(taskToUpdate);
    }

    @Override
    public List<Task> findByFilter(TaskFilter taskFilter) {
        if (taskFilter.getSearchBy() == null) {
            throw new IllegalArgumentException("TaskSearchBy must be specified");
        }
        return taskRepository.findAll(TaskSpecification.withFilter(taskFilter),
                PageRequest.of(
                        taskFilter.getPageNumber(), taskFilter.getPageSize()
                )).getContent();
    }
}
