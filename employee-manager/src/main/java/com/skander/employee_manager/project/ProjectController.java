package com.skander.employee_manager.project;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skander.employee_manager.user.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("project")
@RequiredArgsConstructor
@Tag(name= "project")

public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final ProjectRepository projectRepository;

    private void logFieldType(String fieldName, Object fieldValue) {
        if (fieldValue != null) {
            System.out.println(fieldName + " (" + fieldValue.getClass().getSimpleName() + "): " + fieldValue);
        } else {
            System.out.println(fieldName + " is null");
        }
    }

    @PostMapping("path")
    public ResponseEntity<Integer> saveProject(
        @Valid @RequestBody ProjectRequest request,
        Authentication connectedUser) {
        
     // Adjust this based on your authentication mechanism

    Project project = Project.builder()
        .name(request.name())
        .description(request.description()) 
        .build();

    System.out.println("Saving project:");
    logFieldType("Name", project.getName());
    logFieldType("Description", project.getDescription());

    try {
        projectRepository.save(project);
    } catch (Exception e) {
        System.err.println("Error saving project: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(project.getId()); 
}
// Return the saved project ID

//         System.out.println("Assigned IDs: " + request.assignedTo());
//         Set<User> assignedUsers = new HashSet<>(userService.getUsersByIds(request.assignedTo()));
// System.out.println("Assigned Users: " + assignedUsers);

//             System.out.println("dezodieznodinezidnAGGGGGGGGGGGGGGGGGGG");
//         System.out.println(projectService.save(request, connectedUser));
        
//         return ResponseEntity.ok( projectService.save(request, connectedUser));
    

    @GetMapping("{project-id}")
    public ResponseEntity<ProjectResponse> findProjectById (
        @PathVariable("project-id") Integer projectid
    ){


        return ResponseEntity.ok(projectService.findById(projectid));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProjectResponse>> findAllProjects(
        @RequestParam(name = "page", defaultValue= "0", required= false) int page,
        @RequestParam(name= "size", defaultValue= "10", required= false) int size,
        Authentication connectedUser) {
        return ResponseEntity.ok(projectService.findAllProjects(page, size, connectedUser));
    }
    
    @GetMapping("/assigned")
    public ResponseEntity<PageResponse<ProjectResponse>> findAllProjectsByUser(
        @RequestParam(name = "page", defaultValue= "0", required= false) int page,
        @RequestParam(name= "size", defaultValue= "10", required= false) int size,
        Authentication connectedUser) {
        return ResponseEntity.ok(projectService.findAllProjectsByUser(page, size, connectedUser));
    }
    
    
}
