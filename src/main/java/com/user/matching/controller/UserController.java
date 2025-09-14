package com.user.matching.controller;

import com.user.matching.entity.User;
import com.user.matching.entity.UserPreference;
import com.user.matching.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        String result = userService.registerUser(user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{userId}/preferences")
    public ResponseEntity<User> addPreference(@PathVariable Long userId,
                                              @RequestBody UserPreference preference) {
        return ResponseEntity.ok(userService.addPreference(userId, preference));
    }
}
