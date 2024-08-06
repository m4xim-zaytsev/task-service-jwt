package com.example.task_service_jwt.service;

import com.example.task_service_jwt.entity.User;

import java.util.List;

public interface UserService {
    User findById(Long id);
    List<User> findAll();
    
}
