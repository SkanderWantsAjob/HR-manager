

package com.skander.employee_manager.project;

import java.util.Set;

import com.skander.employee_manager.user.User;

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
public class ProjectResponse {
    private String name;
    private Integer id;
    private String description;
    private Set<User> assignedTo;

}
