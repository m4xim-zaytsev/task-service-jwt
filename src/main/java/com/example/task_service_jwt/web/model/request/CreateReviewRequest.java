package com.example.task_service_jwt.web.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {
    @NotBlank(message = "Topic cannot be blank")
    private String topic;

    @NotBlank(message = "Comment cannot be blank")
    private String comment;

    @NotNull(message = "Task ID cannot be null")
    private Long taskId;
}
