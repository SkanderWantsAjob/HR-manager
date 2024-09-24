package com.skander.employee_manager.department;

public enum Department {
    HR("Human Resources", "Responsible for employee-related functions such as recruitment and payroll."),
    FINANCE("Finance Department", "Handles the organization's financial planning, management, and reporting."),
    IT("Information Technology", "Manages the technology infrastructure and software systems."),
    MARKETING("Marketing Department", "Responsible for promoting products and building brand awareness.");

    private final String departmentName;
    private final String description;

    Department(String departmentName, String description) {
        this.departmentName = departmentName;
        this.description = description;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDescription() {
        return description;
    }
}
