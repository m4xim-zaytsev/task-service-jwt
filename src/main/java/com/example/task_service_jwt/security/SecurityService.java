package com.example.task_service_jwt.security;

import com.example.task_service_jwt.entity.RefreshToken;
import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.exception.AlreadyExistException;
import com.example.task_service_jwt.exception.RefreshTokenException;
import com.example.task_service_jwt.repository.UserRepository;
import com.example.task_service_jwt.security.jwt.JwtUtils;
import com.example.task_service_jwt.service.RefreshTokenService;
import com.example.task_service_jwt.web.model.request.CreateUserRequest;
import com.example.task_service_jwt.web.model.request.LoginRequest;
import com.example.task_service_jwt.web.model.request.RefreshTokenRequest;
import com.example.task_service_jwt.web.model.response.AuthResponse;
import com.example.task_service_jwt.web.model.response.RefreshTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticateUser(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .toList();



        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        AuthResponse response = AuthResponse.builder()
                .id(userDetails.getId())
                .token(jwtUtils.generateJwtToken(userDetails))
                .refreshToken(refreshToken.getToken())
                .username(userDetails.getUsername())
                .roles(roles)
                .build();

        log.info("User {} authenticated successfully with roles {}", userDetails.getUsername(), roles);

        return response;
    }

    public void register(CreateUserRequest request){
        User user = new User();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());

        user.setRoles(request.getRoles());

        if(userRepository.existsByUsername(request.getUsername()))
            throw new AlreadyExistException("Username already exist");

        if (userRepository.existsByEmail(request.getEmail()))
                throw new AlreadyExistException("email already exist");

        userRepository.save(user);
        log.info("User {} registered successfully with email {} ", user.getUsername(), user.getEmail());
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request){
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getId)
                .map(userId -> {
                    User tokenOwner = userRepository.findById(userId)
                            .orElseThrow(()-> new RefreshTokenException("exception trying to get token for userId:\s" + userId));
                    String token = jwtUtils.generateTokenFromUserEmail(tokenOwner.getEmail());
                    return new RefreshTokenResponse(token,refreshTokenService.createRefreshToken(userId).getToken());

                }).orElseThrow(()-> new RefreshTokenException(requestRefreshToken,"refresh token not found"));
    }

    public void logout(){
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails){
            Long userId = userDetails.getId();
            refreshTokenService.deleteByUserId(userId);
        }
    }
}
