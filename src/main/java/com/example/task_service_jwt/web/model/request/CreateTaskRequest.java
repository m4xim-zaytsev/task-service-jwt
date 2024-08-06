package com.example.task_service_jwt.web.model.request;

import com.example.task_service_jwt.entity.TaskPriority;
import com.example.task_service_jwt.entity.TaskStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {
    private String name;
    private String description;
    private TaskStatus status;
    private TaskPriority taskPriority;
    private String authorId;
    private String executorId;
}
