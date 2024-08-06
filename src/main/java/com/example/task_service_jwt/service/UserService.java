package com.example.task_service_jwt.service;

import com.example.task_service_jwt.entity.User;

public interface UserService {
    User findById(Long id);
}
