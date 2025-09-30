package com.user.matching.service;

import com.user.matching.entity.User;
import com.user.matching.entity.UserPreference;
import com.user.matching.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final String USER_ALREADY_EXISTS = "USER ALREADY EXISTS by this Email";

    public String registerUser(User user) {
        if (!(userRepository.existsByEmail(user.getEmail()))) {
            if (user.getPreferences() != null) {
                user. getPreferences().forEach(pref -> pref.setUser(user));
            }
            return userRepository.save(user).getId().toString();
        }
        return USER_ALREADY_EXISTS;
    }

    public User addPreference(UUID userId, List<UserPreference> preferences) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Add new preferences safely
        for (UserPreference preference : preferences) {
            preference.setUser(user); // link preference to user
            user.getPreferences().add(preference); // mutate existing collection
        }

        return userRepository.save(user);
    }

}
