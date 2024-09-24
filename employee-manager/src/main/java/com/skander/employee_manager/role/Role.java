package com.skander.employee_manager.role;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skander.employee_manager.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {


    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    
    @ManyToMany(mappedBy="roles")
    @JsonIgnore
    private List<User> users;

    @Column(nullable = true, updatable= false )
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable=false)
    private LocalDateTime lastModifiedDate;

    public String getName(){
        return this.name;
    }

    
}
