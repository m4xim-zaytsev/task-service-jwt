package com.example.task_service_jwt.service.impl;

import com.example.task_service_jwt.entity.Review;
import com.example.task_service_jwt.entity.Task;
import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.repository.ReviewRepository;
import com.example.task_service_jwt.repository.TaskRepository;
import com.example.task_service_jwt.repository.UserRepository;
import com.example.task_service_jwt.service.ReviewService;
import com.example.task_service_jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public Review create(Review review, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found for username: " + userDetails.getUsername()));
        Task task = taskRepository.findById(review.getTask().getId())
                .orElseThrow(() -> new RuntimeException("Task not found for ID: " + review.getTask().getId()));

        review.setAuthor(user);
        review.setTask(task);

        return reviewRepository.save(review);
    }
}
