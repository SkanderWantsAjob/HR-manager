/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.skander.employee_manager.project;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.skander.employee_manager.user.User;
import com.skander.employee_manager.user.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;

    @Transactional
public Long save(ProjectRequest request, Authentication authentication) {
    Project project = projectMapper.toProject(request, authentication);
    

    // Save the project with the assigned users included
    project = projectRepository.save(project);

    // The project should now have its users properly set before it's saved.
    System.out.println(project.toString());

    return project.getId();
}

    public ProjectResponse findById( Long projectId){
        return projectRepository.findById(projectId)
            .map(projectMapper::toProjectResponse)
            .orElseThrow(() -> new EntityNotFoundException("no project found with that id"));
    }

    public PageResponse<ProjectResponse> findAllProjects(int page, int size, Authentication connectedUser){
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Project> projects = projectRepository.findAllDisplayableProjects(pageable, user.getId());
        List<ProjectResponse> projectResponse = projects.stream()
                                            .map(projectMapper::toProjectResponse)
                                            .toList();
        return new PageResponse<>(projectResponse,
                                projects.getNumber(),
                                projects.getSize(),
                                projects.getNumberOfElements(),
                                projects.getTotalPages(),
                                projects.isFirst(),
                                projects.isLast());
    }
    public PageResponse<ProjectResponse> findAllProjectsByUser(int page, int size, Authentication connectedUser){
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Project> projects = projectRepository.findAll(ProjectSpecification.withAssignedUserId(user.getId()), pageable);
        List<ProjectResponse> projectResponse = projects.stream()
        .map(projectMapper::toProjectResponse)
        .toList();
                return new PageResponse<>(projectResponse,
                projects.getNumber(),
                projects.getSize(),
                projects.getNumberOfElements(),
                projects.getTotalPages(),
                projects.isFirst(),
                projects.isLast());
}

}
