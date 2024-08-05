package com.example.task_service_jwt.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app")
public class AppController {

    @GetMapping("/all")
    public String allAccess(){
        return "Public Response Data";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess(){
        return "ADMIN data";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userAccess(){
        return "USER data";
    }
}
