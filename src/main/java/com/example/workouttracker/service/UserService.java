// src/main/java/com/example/workouttracker/service/UserService.java
package com.example.workouttracker.service;

import com.example.workouttracker.dto.UserDTO;
import com.example.workouttracker.model.User;
import com.example.workouttracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender emailSender;

    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        // Ensure all required fields are set
        if (user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Email and password must not be null");
        }
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public ResponseEntity<?> authenticateUser(String email, String password) {
        User user = findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("{\"error\": \"Email not found\"}");
        }
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.badRequest().body("{\"error\": \"Incorrect password\"}");
        }
        return ResponseEntity.ok(convertToDTO(user));
    }

    public ResponseEntity<?> resetPassword(String email) {
        User user = findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("{\"error\": \"Email not found\"}");
        }

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userRepository.save(user);

        sendPasswordResetEmail(user.getEmail(), resetToken);

        return ResponseEntity.ok().body("{\"message\": \"Password reset email sent\"}");
    }

    private void sendPasswordResetEmail(String email, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@workouttracker.com");
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, please use the following token: " + resetToken);
        emailSender.send(message);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }
}