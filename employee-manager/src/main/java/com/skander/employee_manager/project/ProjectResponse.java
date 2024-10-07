

package com.skander.employee_manager.project;

import java.util.List;

import com.skander.employee_manager.user.UserResponse;

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
    private Long id;
    private String description;
    private List<UserResponse> assignedTo;

}
