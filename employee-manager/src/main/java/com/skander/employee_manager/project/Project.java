package com.skander.employee_manager.project;



import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.skander.employee_manager.common.BaseEntity;
import com.skander.employee_manager.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)

public class Project extends BaseEntity  {
   
    private String name;
    private String description;


    @ManyToMany(mappedBy="projects")
    private Set<User> assignedTo;

    public String UserToString(){
        String b= "";
        for (User user : assignedTo) {
            
            b += user.toString();
        }
        return b;
    }
    @Override
    public String toString() {
        return "Project{" +
                "id=" + this.getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", assignedTo=" + assignedTo +
                ", createdDate=" + this.getCreatedDate() +
                '}';
    }
    

    
    
}
