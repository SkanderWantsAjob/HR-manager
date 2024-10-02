package com.skander.employee_manager.project;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.skander.employee_manager.user.User;
import com.skander.employee_manager.user.UserService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ProjectMapper {
    private final UserService userService;

    public Project toProject(ProjectRequest request, Authentication assigner) {
        System.out.println("AAAAAAAAAAAAAAA2");
        
        User projectCreater = (User) assigner.getPrincipal();
        Set<User> assignedUsers = new HashSet <> (userService.getUsersByIds(request.assignedTo()));
        System.out.println("AAAAAAAAAAAAAAA3");
        assignedUsers.add(projectCreater);
        System.out.print(request.name() + "  " +  request.description());
        System.out.println("AAAAAAAAAAAAAAA4");

        
                return Project.builder()
                
                .build();

    }
    public ProjectResponse  toProjectResponse(Project project){
        return ProjectResponse.builder()
            .id(project.getId())
            .name(project.getName())
            .description(project.getDescription())
            .assignedTo(project.getAssignedTo())
            .build();
    }
    
}
