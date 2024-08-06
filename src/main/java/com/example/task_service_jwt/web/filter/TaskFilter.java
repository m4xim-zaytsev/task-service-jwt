package com.example.task_service_jwt.web.filter;

import com.example.task_service_jwt.entity.TaskPriority;
import com.example.task_service_jwt.entity.TaskStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskFilter {
    private String name;
    private String description;
    private TaskStatus status;
    private TaskPriority taskPriority;

    @NotNull(message = "The 'searchBy' field must be specified.")
    private TaskSearchBy searchBy;

    @NotNull(message = "The 'pageNumber' field is required. Please provide a page number.")
    @Min(value = 0, message = "The 'pageNumber' must be at least 0.")
    private Integer pageNumber;

    @NotNull(message = "The 'pageSize' field is required. Please provide a page size.")
    @Min(value = 1, message = "The 'pageSize' must be greater than 0.")
    private Integer pageSize;

    @NotNull(message = "The 'userId' field must be specified.")
    private Long userId;
}
