package com.example.task_service_jwt.repository;

import com.example.task_service_jwt.entity.Review;
import com.example.task_service_jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

}
