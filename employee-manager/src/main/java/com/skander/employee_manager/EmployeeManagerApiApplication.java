package com.skander.employee_manager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import com.skander.employee_manager.role.Role;
import com.skander.employee_manager.role.RoleRepository;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef="auditorAware")
@EnableAsync
public class EmployeeManagerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagerApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository){
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(
					Role.builder().name("USER").build()
				);
			}
		};
	}

}
