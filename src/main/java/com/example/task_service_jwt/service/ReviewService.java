package com.example.task_service_jwt.service;

import com.example.task_service_jwt.entity.Review;
import org.springframework.security.core.userdetails.UserDetails;

public interface ReviewService {
    Review create(Review review, UserDetails userDetails);
}
