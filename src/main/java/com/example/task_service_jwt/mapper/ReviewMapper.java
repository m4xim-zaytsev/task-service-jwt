package com.example.task_service_jwt.mapper;

import com.example.task_service_jwt.entity.Review;
import com.example.task_service_jwt.entity.Task;
import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.repository.TaskRepository;
import com.example.task_service_jwt.repository.UserRepository;
import com.example.task_service_jwt.web.model.request.CreateReviewRequest;
import com.example.task_service_jwt.web.model.response.ReviewResponse;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class})
public abstract class ReviewMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Mapping(target = "author", expression = "java(mapAuthor(userDetails))")
    @Mapping(target = "task", expression = "java(mapTask(request.getTaskId()))")
    @Mapping(target = "topic", source = "request.topic")
    @Mapping(target = "comment", source = "request.comment")
    public abstract Review requestToReview(CreateReviewRequest request, @Context UserDetails userDetails);

    @Mapping(target = "user", source = "author")
    @Mapping(target = "taskId", source = "task.id")
    public abstract ReviewResponse reviewToResponse(Review review);

    protected User mapAuthor(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found for username: " + userDetails.getUsername()));
    }

    protected Task mapTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found for ID: " + taskId));
    }
}
