package com.skander.employee_manager.project;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.skander.employee_manager.user.User;
import com.skander.employee_manager.user.UserResponse;
import com.skander.employee_manager.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectMapper {
    private final UserService userService;


    public Project toProject(ProjectRequest request, Authentication assigner) {
    User projectCreater = (User) assigner.getPrincipal();
    projectCreater =userService.getUserById(projectCreater.getId());
    List<User> assignedUsers = new ArrayList<>(userService.getUsersByIds(request.assignedTo()));

    // Add the project creator to the list of assigned users
    assignedUsers.add(projectCreater);
    assignedUsers.remove(1);


    // Create the project and set the assigned users right away
    Project project = Project.builder()
            .name(request.name())
            .description(request.description())
            .assignedTo(assignedUsers)
            .build();


    

    

    return project;
}

    public ProjectResponse  toProjectResponse(Project project){

        List<UserResponse> assignedUsers = project.getAssignedTo().stream()
        .map(UserResponse::fromUser) 
        .collect(Collectors.toList());

        return ProjectResponse.builder()
            .id(project.getId())
            .name(project.getName())
            .description(project.getDescription())
            .assignedTo(assignedUsers)
            .build();
    }
    
}
