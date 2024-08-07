package com.example.workouttracker.controller;

import com.example.workouttracker.model.User;
import com.example.workouttracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        return userService.authenticateUser(user.getEmail(), user.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // In a real application, you might invalidate the session or token here
        return ResponseEntity.ok().body("{\"message\": \"Logged out successfully\"}");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody User user) {
        return userService.resetPassword(user.getEmail());
    }
}