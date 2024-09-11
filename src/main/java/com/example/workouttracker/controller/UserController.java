package com.example.workouttracker.controller;

import com.example.workouttracker.dto.UserDTO;
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
    public ResponseEntity<UserDTO> signUp(@RequestBody UserDTO user) {
        UserDTO createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO user) {
        return (ResponseEntity<UserDTO>) userService.authenticateUser(user.getEmail(), user.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // In a real application, you might invalidate the session or token here
        return ResponseEntity.ok().body("{\"message\": \"Logged out successfully\"}");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<UserDTO> forgotPassword(@RequestBody UserDTO user) {
        return (ResponseEntity<UserDTO>) userService.resetPassword(user.getEmail());
    }
}