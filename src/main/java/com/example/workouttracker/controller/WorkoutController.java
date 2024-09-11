// src/main/java/com/example/workouttracker/controller/WorkoutController.java
package com.example.workouttracker.controller;

import com.example.workouttracker.dto.WorkoutDTO;
import com.example.workouttracker.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {
    @Autowired
    private WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<WorkoutDTO> createWorkout(@RequestBody WorkoutDTO workoutDTO) {
        WorkoutDTO createdWorkout = workoutService.createWorkout(workoutDTO);
        return ResponseEntity.ok(createdWorkout);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkoutDTO>> getWorkoutsByUserId(@PathVariable Long userId) {
        List<WorkoutDTO> workouts = workoutService.getWorkoutsByUserId(userId);
        return ResponseEntity.ok(workouts);
    }
}