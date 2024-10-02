

package com.skander.employee_manager.project;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProjectRequest (
    @NotNull(message= "102")
    @NotEmpty(message = "102")
    String description,

    @NotNull(message = "101")
    @NotEmpty(message = "101")
    String name,

    @NotNull(message= "103")
    @NotEmpty(message = "103")
    Set<Integer> assignedTo




){}
