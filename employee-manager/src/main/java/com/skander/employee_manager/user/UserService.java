package com.skander.employee_manager.user;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Fetch users by their IDs
    public Set<User> getUsersByIds(Set<Integer> userIds) {
        return userIds.stream()
                    .map(userRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
    }

    // Fetch user by email (for assigner)
    public User getUserById(Integer Id) {
        return userRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
