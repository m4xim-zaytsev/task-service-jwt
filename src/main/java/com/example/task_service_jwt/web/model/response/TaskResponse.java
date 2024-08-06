package com.example.task_service_jwt.web.model.response;

import com.example.task_service_jwt.entity.TaskPriority;
import com.example.task_service_jwt.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private TaskPriority taskPriority;
    private UserResponse author;
    private UserResponse executor;
}
