package com.example.task_service_jwt.repository;

import com.example.task_service_jwt.entity.Task;
import com.example.task_service_jwt.entity.TaskPriority;
import com.example.task_service_jwt.entity.TaskStatus;
import com.example.task_service_jwt.web.filter.TaskFilter;
import com.example.task_service_jwt.web.filter.TaskSearchBy;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> withFilter(TaskFilter filter) {
        if (filter.getSearchBy() == null) {
            throw new IllegalArgumentException("TaskSearchBy must be specified");
        }

        return Specification
                .where(byName(filter.getName()))
                .and(byDescription(filter.getDescription()))
                .and(byStatus(filter.getStatus()))
                .and(byPriority(filter.getTaskPriority()))
                .and(byUserId(filter.getUserId(), filter.getSearchBy()));
    }

    private static Specification<Task> byName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    private static Specification<Task> byDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null || description.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
        };
    }

    private static Specification<Task> byStatus(TaskStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    private static Specification<Task> byPriority(TaskPriority priority) {
        return (root, query, criteriaBuilder) -> {
            if (priority == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("priority"), priority);
        };
    }

    private static Specification<Task> byUserId(Long userId, TaskSearchBy searchBy) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            if (searchBy == TaskSearchBy.AUTHOR) {
                return criteriaBuilder.equal(root.get("author").get("id"), userId);
            } else if (searchBy == TaskSearchBy.EXECUTOR) {
                return criteriaBuilder.equal(root.get("executor").get("id"), userId);
            }
            return criteriaBuilder.conjunction();
        };
    }
}