package com.skander.employee_manager.project;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    Optional<Project> findByName (String project);

    @Query("""
            SELECT project
            FROM Project project
            """)
    public Page<Project> findAllDisplayableProjects(Pageable pageable, Long userId);
    
}
