package com.skander.employee_manager.project;
import org.springframework.data.jpa.domain.Specification;

public class ProjectSpecification {

    public static Specification<Project> withAssignedUserId(Integer userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isMember(userId, root.get("assignedTo"));
    }
}
