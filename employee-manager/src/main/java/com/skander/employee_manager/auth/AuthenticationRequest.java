/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.skander.employee_manager.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
class AuthenticationRequest {
    @NotEmpty(message="email is mandatory")
    @NotBlank(message="email is mandatory")
    private String email;

    @NotEmpty(message="password is mandatory")
    @NotBlank(message="password is mandatory")
    @Size(min = 8, message= "password should be at least 8 characters")
    private String password;

}