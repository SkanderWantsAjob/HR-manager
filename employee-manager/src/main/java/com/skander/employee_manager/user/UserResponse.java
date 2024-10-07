package com.skander.employee_manager.user;

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
public class UserResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .email(user.getEmail())
            .build();
    }
}
