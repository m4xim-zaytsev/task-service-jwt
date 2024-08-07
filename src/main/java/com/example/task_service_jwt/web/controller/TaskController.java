package com.example.task_service_jwt.web.controller;

import com.example.task_service_jwt.entity.Task;
import com.example.task_service_jwt.entity.TaskStatus;
import com.example.task_service_jwt.mapper.TaskMapper;
import com.example.task_service_jwt.service.TaskService;
import com.example.task_service_jwt.web.filter.TaskFilter;
import com.example.task_service_jwt.web.model.request.CreateTaskRequest;
import com.example.task_service_jwt.web.model.response.SimpleResponse;
import com.example.task_service_jwt.web.model.response.TaskResponse;
import com.example.task_service_jwt.web.model.response.TaskResponseList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping()
    public ResponseEntity<TaskResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestBody @Valid CreateTaskRequest request){
        Task createdTask = taskService.createTask(taskMapper.toTask(request),userDetails);
        TaskResponse response = taskMapper.toTaskResponse(createdTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<TaskResponseList> getAll(){
        return ResponseEntity.ok(taskMapper.taskListToTaskListResponse(taskService.findAll()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @RequestBody @Valid CreateTaskRequest request,
                                               @AuthenticationPrincipal UserDetails userDetails){
        Task updatedTask = taskService.update(id,taskMapper.toTask(request),userDetails);
        TaskResponse taskResponse = taskMapper.toTaskResponse(updatedTask);
        return ResponseEntity.ok(taskResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable Long id){
        taskService.delete(id,userDetails);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter-by")
    public ResponseEntity<TaskResponseList> findByFilter(@Valid TaskFilter taskFilter) {
        return ResponseEntity.ok(taskMapper.taskListToTaskListResponse(
                taskService.findByFilter(taskFilter)
        ));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<TaskResponse> updateStatus(@PathVariable Long id, @RequestParam TaskStatus status,
                                               @AuthenticationPrincipal UserDetails userDetails) {

        Task updatedTask = taskService.updateStatus(status,id,userDetails);
        return ResponseEntity.ok(taskMapper.toTaskResponse(updatedTask));
    }









}
