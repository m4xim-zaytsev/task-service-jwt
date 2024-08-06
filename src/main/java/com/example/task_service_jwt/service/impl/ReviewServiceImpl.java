package com.example.task_service_jwt.service.impl;

import com.example.task_service_jwt.entity.Review;
import com.example.task_service_jwt.entity.Task;
import com.example.task_service_jwt.entity.TaskStatus;
import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.exception.EntityNotFoundException;
import com.example.task_service_jwt.exception.IllegalAccessException;
import com.example.task_service_jwt.repository.ReviewRepository;
import com.example.task_service_jwt.repository.TaskRepository;
import com.example.task_service_jwt.repository.UserRepository;
import com.example.task_service_jwt.service.ReviewService;
import com.example.task_service_jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

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

    @Override
    public Review update(Long id, Review review, UserDetails userDetails) {
        Review toUpdate = findById(id);
        if(!toUpdate.getAuthor().getUsername().equals(userDetails.getUsername()))
            throw new IllegalAccessException("only author can update review");

        toUpdate.setTopic(review.getTopic());
        toUpdate.setComment(review.getComment());

        return reviewRepository.save(toUpdate);
    }

    @Override
    public Review findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        MessageFormat.format("Review with id: {0} not found",id)
                ));
    }

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public void deleteById(Long id, UserDetails userDetails) {
        Review review = findById(id);
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found for username: " + userDetails.getUsername()));

        if(review.getAuthor().getEmail().equals(user.getEmail()))
            reviewRepository.deleteById(id);
    }

}
