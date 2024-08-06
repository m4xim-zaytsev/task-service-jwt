package com.example.task_service_jwt.repository;

import com.example.task_service_jwt.entity.Task;
import com.example.task_service_jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task> {
    List<Task> findByAuthorId(Long id);
    List<Task> findByExecutorId(Long id);
}
