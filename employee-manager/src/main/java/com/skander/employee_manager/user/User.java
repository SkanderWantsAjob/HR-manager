package com.skander.employee_manager.user;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.skander.employee_manager.project.Project;
import com.skander.employee_manager.role.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
@Table(name= "user_app")
@EntityListeners(AuditingEntityListener.class)



public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue
    private Integer id;

    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    
    @Column (unique = true)
    private String email;
    private String password;
    private boolean accountLocked;
    private boolean enabled;
    // @Enumerated(EnumType.STRING)
    // @Column
    // private Department department;

    @ManyToMany
    @JoinTable(
    name = "user_projects",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "project_id")
)
    private Set<Project> projects;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @CreatedDate
    @Column(nullable = true, updatable= false )
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable=false)
    private LocalDateTime lastModifiedDate;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
        .stream()
        .map(r-> new SimpleGrantedAuthority(r.getName()))
        .collect(Collectors.toList()) ;
    }

  
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public String fullName() {
        return getFirstname() + " " + getLastname();
    }


    public String getFullName() {
        return this.firstname + " " + this.lastname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


    
    
}
