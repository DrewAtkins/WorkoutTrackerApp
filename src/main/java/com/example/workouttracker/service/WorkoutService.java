// src/main/java/com/example/workouttracker/service/WorkoutService.java
package com.example.workouttracker.service;

import com.example.workouttracker.dto.WorkoutDTO;
import com.example.workouttracker.model.User;
import com.example.workouttracker.model.Workout;
import com.example.workouttracker.repository.UserRepository;
import com.example.workouttracker.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutService {
    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository;

    public WorkoutDTO createWorkout(WorkoutDTO workoutDTO) {
        Workout workout = convertToEntity(workoutDTO);
        Workout savedWorkout = workoutRepository.save(workout);
        return convertToDTO(savedWorkout);
    }

    public List<WorkoutDTO> getWorkoutsByUserId(Long userId) {
        return workoutRepository.findByUserIdOrderByDateDesc(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private WorkoutDTO convertToDTO(Workout workout) {
        WorkoutDTO workoutDTO = new WorkoutDTO();
        workoutDTO.setId(workout.getId());
        workoutDTO.setUserId(workout.getUser().getId());
        workoutDTO.setDate(workout.getDate());
        workoutDTO.setDescription(workout.getDescription());
        return workoutDTO;
    }

    private Workout convertToEntity(WorkoutDTO workoutDTO) {
        Workout workout = new Workout();
        workout.setId(workoutDTO.getId());
        workout.setDate(workoutDTO.getDate());
        workout.setDescription(workoutDTO.getDescription());
        User user = userRepository.findById(workoutDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        workout.setUser(user);
        return workout;
    }
}