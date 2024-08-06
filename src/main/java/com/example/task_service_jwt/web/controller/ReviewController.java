package com.example.task_service_jwt.web.controller;

import com.example.task_service_jwt.entity.Review;
import com.example.task_service_jwt.mapper.ReviewMapper;
import com.example.task_service_jwt.service.ReviewService;
import com.example.task_service_jwt.web.model.request.CreateReviewRequest;
import com.example.task_service_jwt.web.model.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping
    public ResponseEntity<ReviewResponse> add(@RequestBody CreateReviewRequest request,
                                              @AuthenticationPrincipal UserDetails userDetails){
        Review createdReview = reviewService.create(reviewMapper.requestToReview(request,userDetails),userDetails);
        ReviewResponse response = reviewMapper.reviewToResponse(createdReview);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
