package com.example.task_service_jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.task_service_jwt.entity.*;
import com.example.task_service_jwt.mapper.TaskMapper;
import com.example.task_service_jwt.repository.TaskRepository;
import com.example.task_service_jwt.repository.UserRepository;
import com.example.task_service_jwt.web.model.request.CreateTaskRequest;
import com.example.task_service_jwt.web.model.response.TaskResponse;
import com.example.task_service_jwt.web.model.response.TaskResponseList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskMapper taskMapper;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testCreateTask() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
        user.setRoles(Collections.singleton(RoleType.ROLE_USER));
        userRepository.save(user);

        CreateTaskRequest createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setName("Test Task");
        createTaskRequest.setDescription("This is a test task");
        createTaskRequest.setStatus(TaskStatus.IN_PROGRESS);
        createTaskRequest.setTaskPriority(TaskPriority.HIGH);
        createTaskRequest.setExecutorId(user.getId().toString());

        MvcResult result = mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Test Task"))
                .andExpect(jsonPath("$.description").value("This is a test task"))
                .andReturn();

        // Проверка, что задача была добавлена в БД
        String responseBody = result.getResponse().getContentAsString();
        TaskResponse taskResponse = objectMapper.readValue(responseBody, TaskResponse.class);
        Task task = taskRepository.findById(taskResponse.getId()).orElse(null);

        assertNotNull(task);
        assertEquals("Test Task", task.getName());
        assertEquals("This is a test task", task.getDescription());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetAllTasks() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
        user.setRoles(Collections.singleton(RoleType.ROLE_USER));
        userRepository.save(user);

        Task task1 = new Task();
        task1.setName("Test Task 1");
        task1.setDescription("This is the first test task");
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setTaskPriority(TaskPriority.HIGH);
        task1.setAuthor(user);
        task1.setExecutor(user);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setName("Test Task 2");
        task2.setDescription("This is the second test task");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setTaskPriority(TaskPriority.LOW);
        task2.setAuthor(user);
        task2.setExecutor(user);
        taskRepository.save(task2);

        List<Task> expectedTasks = List.of(task1, task2);
        List<TaskResponse> expectedTaskResponses = taskMapper.taskListToTaskListResponse(expectedTasks).getTaskResponses();

        MvcResult result = mockMvc.perform(get("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskResponses").isArray())
                .andReturn();

        // Проверка, что возвращаемый список равен ожидаемому
        String responseBody = result.getResponse().getContentAsString();
        TaskResponseList taskResponseList = objectMapper.readValue(responseBody, TaskResponseList.class);
        List<TaskResponse> actualTaskResponses = taskResponseList.getTaskResponses();

        assertEquals(expectedTaskResponses.size(), actualTaskResponses.size());

    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testUpdateTask() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
        user.setRoles(Collections.singleton(RoleType.ROLE_USER));
        userRepository.save(user);

        Task task = new Task();
        task.setName("Original Task");
        task.setDescription("This is the original task");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setTaskPriority(TaskPriority.HIGH);
        task.setAuthor(user);
        task.setExecutor(user);
        taskRepository.save(task);

        CreateTaskRequest updateTaskRequest = new CreateTaskRequest();
        updateTaskRequest.setName("Updated Task");
        updateTaskRequest.setDescription("This is the updated task");
        updateTaskRequest.setStatus(TaskStatus.DONE);
        updateTaskRequest.setTaskPriority(TaskPriority.LOW);
        updateTaskRequest.setExecutorId(user.getId().toString());

        MvcResult result = mockMvc.perform(put("/api/v1/task/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTaskRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.name").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("This is the updated task"))
                .andExpect(jsonPath("$.status").value(TaskStatus.DONE.toString()))
                .andExpect(jsonPath("$.taskPriority").value(TaskPriority.LOW.toString()))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        TaskResponse taskResponse = objectMapper.readValue(responseBody, TaskResponse.class);
        Task updatedTask = taskRepository.findById(taskResponse.getId()).orElse(null);

        assertNotNull(updatedTask);
        assertEquals("Updated Task", updatedTask.getName());
        assertEquals("This is the updated task", updatedTask.getDescription());
        assertEquals(TaskStatus.DONE, updatedTask.getStatus());
        assertEquals(TaskPriority.LOW, updatedTask.getTaskPriority());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testDeleteTask() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
        user.setRoles(Collections.singleton(RoleType.ROLE_USER));
        userRepository.save(user);

        Task task = new Task();
        task.setName("Task to be deleted");
        task.setDescription("This task will be deleted");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setTaskPriority(TaskPriority.HIGH);
        task.setAuthor(user);
        task.setExecutor(user);
        taskRepository.save(task);

        mockMvc.perform(delete("/api/v1/task/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        Task deletedTask = taskRepository.findById(task.getId()).orElse(null);
        assertNull(deletedTask);
    }
}

