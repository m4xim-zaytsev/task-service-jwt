package com.example.task_service_jwt.web.model.request;

import com.example.task_service_jwt.entity.TaskPriority;
import com.example.task_service_jwt.entity.TaskStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must be less than or equal to 100 characters")
    private String name;

    @Size(max = 500, message = "Description must be less than or equal to 500 characters")
    private String description;

    @NotNull(message = "Status cannot be null")
    private TaskStatus status;

    @NotNull(message = "Task priority cannot be null")
    private TaskPriority taskPriority;

    @NotBlank(message = "Author ID cannot be blank")
    private String authorId;

    @NotBlank(message = "Executor ID cannot be blank")
    private String executorId;
}
