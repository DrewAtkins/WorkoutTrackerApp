package com.example.workouttracker.service;

import com.example.workouttracker.model.Workout;
import com.example.workouttracker.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutService {
    @Autowired
    private WorkoutRepository workoutRepository;

    public Workout createWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }

    public List<Workout> getWorkoutsByUserId(Long userId) {
        return workoutRepository.findByUserIdOrderByDateDesc(userId);
    }
}