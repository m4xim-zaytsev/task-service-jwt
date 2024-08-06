package com.example.task_service_jwt.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseList {
    List<ReviewResponse> reviewResponses = new ArrayList<>();
}
