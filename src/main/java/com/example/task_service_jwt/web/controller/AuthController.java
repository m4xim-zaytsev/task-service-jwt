package com.example.task_service_jwt.web.controller;

import com.example.task_service_jwt.exception.AlreadyExistException;
import com.example.task_service_jwt.repository.UserRepository;
import com.example.task_service_jwt.security.SecurityService;
import com.example.task_service_jwt.web.model.request.CreateUserRequest;
import com.example.task_service_jwt.web.model.request.LoginRequest;
import com.example.task_service_jwt.web.model.request.RefreshTokenRequest;
import com.example.task_service_jwt.web.model.response.AuthResponse;
import com.example.task_service_jwt.web.model.response.RefreshTokenResponse;
import com.example.task_service_jwt.web.model.response.SimpleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final SecurityService securityService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(securityService.authenticateUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<SimpleResponse> registerUser(@RequestBody CreateUserRequest request){
        securityService.register(request);
        return ResponseEntity.ok(new SimpleResponse("user created"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request){
        return ResponseEntity.ok(securityService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponse> logoutUser(@AuthenticationPrincipal UserDetails userDetails){
        securityService.logout();
        return ResponseEntity.ok(new SimpleResponse("User logout, username is:\s"+userDetails.getUsername()));
    }
}
