package com.example.workouttracker.service;

import com.example.workouttracker.dto.WorkoutDTO;
import com.example.workouttracker.model.User;
import com.example.workouttracker.model.Workout;
import com.example.workouttracker.repository.UserRepository;
import com.example.workouttracker.repository.WorkoutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutService {
    private static final Logger logger = LoggerFactory.getLogger(WorkoutService.class);

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository;

    public WorkoutDTO createWorkout(WorkoutDTO workoutDTO) {
        logger.info("Creating workout: {}", workoutDTO);
        if (workoutDTO == null) {
            logger.error("WorkoutDTO is null");
            throw new IllegalArgumentException("WorkoutDTO cannot be null");
        }
        if (workoutDTO.getUserId() == null) {
            logger.error("UserId is null in WorkoutDTO");
            throw new IllegalArgumentException("UserId cannot be null");
        }
        try {
            Workout workout = convertToEntity(workoutDTO);
            Workout savedWorkout = workoutRepository.save(workout);
            logger.info("Workout created successfully: {}", savedWorkout);
            return convertToDTO(savedWorkout);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating workout: {}", e.getMessage());
            throw e; // Rethrow IllegalArgumentException
        } catch (Exception e) {
            logger.error("Unexpected error creating workout", e);
            throw new RuntimeException("Failed to create workout", e);
        }
    }

    public List<WorkoutDTO> getWorkoutsByUserId(Long userId) {
        logger.info("Fetching workouts for user: {}", userId);
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
        logger.info("Converting WorkoutDTO to Workout entity: {}", workoutDTO);
        Workout workout = new Workout();
        workout.setId(workoutDTO.getId());
        workout.setDate(workoutDTO.getDate());
        workout.setDescription(workoutDTO.getDescription());
        
        if (workoutDTO.getUserId() == null) {
            logger.error("UserId is null in WorkoutDTO");
            throw new IllegalArgumentException("UserId cannot be null");
        }
        
        User user = userRepository.findById(workoutDTO.getUserId())
                .orElseThrow(() -> {
                    logger.error("User not found for id: {}", workoutDTO.getUserId());
                    return new IllegalArgumentException("User not found for id: " + workoutDTO.getUserId());
                });
        workout.setUser(user);
        logger.info("Workout entity created: {}", workout);
        return workout;
    }
}