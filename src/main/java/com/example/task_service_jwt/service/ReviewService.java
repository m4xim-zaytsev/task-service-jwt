package com.example.task_service_jwt.service;

import com.example.task_service_jwt.entity.Review;
import com.example.task_service_jwt.entity.TaskStatus;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ReviewService {
    Review create(Review review, UserDetails userDetails);
    Review update(Long id, Review review,UserDetails userDetails);
    Review findById(Long id);
    List<Review> findAll();
    void deleteById(Long id,UserDetails userDetails);

}
