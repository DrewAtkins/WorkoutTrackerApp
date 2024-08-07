package com.example.workouttracker.controller;

import com.example.workouttracker.model.Workout;
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
    public ResponseEntity<Workout> createWorkout(@RequestBody Workout workout) {
        Workout createdWorkout = workoutService.createWorkout(workout);
        return ResponseEntity.ok(createdWorkout);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Workout>> getWorkoutsByUserId(@PathVariable Long userId) {
        List<Workout> workouts = workoutService.getWorkoutsByUserId(userId);
        return ResponseEntity.ok(workouts);
    }
}