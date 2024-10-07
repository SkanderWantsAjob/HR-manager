package com.skander.employee_manager.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Fetch users by their IDs
    public List<User> getUsersByIds(List<Long> userIds) {
        return userIds.stream()
                    .map(userRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
    }

    // Fetch user by email (for assigner)
    public User getUserById(Long Id) {
        return userRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
