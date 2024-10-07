package com.skander.employee_manager.project;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class ProjectSpecification {

    public static Specification<Project> withAssignedUserId(Long userId) {
        return (root, query, criteriaBuilder) -> {
            // Join the assignedTo collection
            Join<Object, Object> assignedUsers = root.join("assignedTo", JoinType.INNER);
            // Compare the userId with the user's id in the assignedTo collection
            return criteriaBuilder.equal(assignedUsers.get("id"), userId);
        };
    }
}
