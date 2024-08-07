package com.example.task_service_jwt.mapper;

import com.example.task_service_jwt.entity.Task;
import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.repository.UserRepository;
import com.example.task_service_jwt.web.model.request.CreateTaskRequest;
import com.example.task_service_jwt.web.model.response.TaskResponse;
import com.example.task_service_jwt.web.model.response.TaskResponseList;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class, ReviewMapper.class})
public abstract class TaskMapper {

    @Autowired
    private UserRepository userRepository;

    @Mapping(target = "executor", source = "executorId", qualifiedByName = "mapExecutor")
    public abstract Task toTask(CreateTaskRequest createTaskRequest);
    
    public abstract TaskResponse toTaskResponse(Task task);

    public TaskResponseList taskListToTaskListResponse(List<Task> tasks) {
        List<TaskResponse> taskResponses = tasks.stream()
                .map(this::toTaskResponse)
                .collect(Collectors.toList());
        return new TaskResponseList(taskResponses);
    }

    @Named("mapExecutor")
    protected User mapExecutor(String executorId) {
        if (executorId == null)
            return null;

        return userRepository.findById(Long.valueOf(executorId))
                .orElseThrow(() -> new RuntimeException("Executor not found for ID: " + executorId));
    }
}
