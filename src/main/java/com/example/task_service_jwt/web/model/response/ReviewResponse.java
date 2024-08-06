package com.example.task_service_jwt.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private String topic;
    private String comment;
    private UserResponse user;
    private Long taskId;
}
