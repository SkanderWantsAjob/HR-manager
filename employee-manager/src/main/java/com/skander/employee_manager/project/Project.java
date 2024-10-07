package com.skander.employee_manager.project;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.skander.employee_manager.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)

public class Project   {
   
    @Id
    @GeneratedValue
    protected Long id; 
    private String name;
    private String description;


    
    @ManyToMany(fetch = FetchType.LAZY, cascade={CascadeType.MERGE , CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
    name = "user_projects",
    joinColumns = @JoinColumn(name = "project_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id")
)
    private List<User> assignedTo;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    public String UserToString(){
        String b= "";
        for (User user : assignedTo) {
            
            b += user.toString();
        }
        return b;
    } 
    @CreatedBy
    @Column(nullable = true, updatable = false)
    private Long createdBy;
    @Override
    public String toString() {
        return "Project{" +
                "id=" + this.getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", assignedTo=" + assignedTo +
                ", createdBy=" + this.getCreatedBy() +
                '}';
    }

    public void addAssignedTo(User user) {
        if (assignedTo == null) {
            assignedTo = new ArrayList<>();
        }
        
        
        if (!assignedTo.contains(user)) {
            assignedTo.add(user);
            
            
            if (user.getProjects() == null) {
                user.setProjects(new ArrayList<>()); // Ensure it's initialized
            }
            
            
            if (!user.getProjects().contains(this)) {
                user.getProjects().add(this);
            }
        }
    }
    }
    

