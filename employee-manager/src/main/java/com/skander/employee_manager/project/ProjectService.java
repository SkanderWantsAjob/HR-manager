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

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public Integer save(ProjectRequest request, Authentication authentication){
    Project project = projectMapper.toProject(request, authentication);
    System.out.println("pipo" + project.toString());
    System.out.println("papo "+ projectRepository.save(project));
    return null;

    }

    public ProjectResponse findById( Integer projectId){
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
