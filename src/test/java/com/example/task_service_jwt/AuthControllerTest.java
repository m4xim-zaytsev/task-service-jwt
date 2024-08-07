package com.example.task_service_jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.task_service_jwt.entity.RoleType;
import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.repository.UserRepository;
import com.example.task_service_jwt.web.model.request.CreateUserRequest;
import com.example.task_service_jwt.web.model.request.LoginRequest;
import com.example.task_service_jwt.web.model.response.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Rollback
    public void testRegisterUser() throws Exception {
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .username("user")
                .email("user@example.com")
                .password("user")
                .roles(Set.of(RoleType.ROLE_USER))
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("user created"));

        User user = userRepository.findByEmail("user@example.com").orElseThrow();
        assertNotNull(user);
        assertEquals("user", user.getUsername());
    }

    @Test
    @Rollback
    public void testAuthUser() throws Exception {
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .username("user")
                .email("user@example.com")
                .password("user")
                .roles(Set.of(RoleType.ROLE_USER))
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isOk());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("user");

        MvcResult result = mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        // Извлекаем токен из ответа
        String responseBody = result.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(responseBody, AuthResponse.class);
        String token = authResponse.getToken();

        // Проверка, что токен не пустой и пользователь существует в БД
        assertNotNull(token);
        User user = userRepository.findByEmail("user@example.com").orElseThrow();
        assertNotNull(user);
        assertEquals("user", user.getUsername());
    }
}
