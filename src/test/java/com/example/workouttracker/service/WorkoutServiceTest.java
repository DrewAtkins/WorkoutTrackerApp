package com.example.workouttracker.service;

import com.example.workouttracker.dto.WorkoutDTO;
import com.example.workouttracker.model.User;
import com.example.workouttracker.model.Workout;
import com.example.workouttracker.repository.UserRepository;
import com.example.workouttracker.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WorkoutService workoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createWorkout_Success() {
        // Arrange
        WorkoutDTO workoutDTO = new WorkoutDTO();
        workoutDTO.setUserId(1L);
        workoutDTO.setDate(LocalDateTime.now());
        workoutDTO.setDescription("Test workout");

        User user = new User();
        user.setId(1L);

        Workout savedWorkout = new Workout();
        savedWorkout.setId(1L);
        savedWorkout.setUser(user);
        savedWorkout.setDate(workoutDTO.getDate());
        savedWorkout.setDescription(workoutDTO.getDescription());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(workoutRepository.save(any(Workout.class))).thenReturn(savedWorkout);

        // Act
        WorkoutDTO result = workoutService.createWorkout(workoutDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getUserId());
        assertEquals(workoutDTO.getDate(), result.getDate());
        assertEquals(workoutDTO.getDescription(), result.getDescription());

        verify(userRepository, times(1)).findById(1L);
        verify(workoutRepository, times(1)).save(any(Workout.class));
    }

    @Test
    void createWorkout_UserNotFound() {
        // Arrange
        WorkoutDTO workoutDTO = new WorkoutDTO();
        workoutDTO.setUserId(1L);
        workoutDTO.setDate(LocalDateTime.now());
        workoutDTO.setDescription("Test workout");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> workoutService.createWorkout(workoutDTO));

        verify(userRepository, times(1)).findById(1L);
        verify(workoutRepository, never()).save(any(Workout.class));
    }

    @Test
    void createWorkout_NullUserId() {
        // Arrange
        WorkoutDTO workoutDTO = new WorkoutDTO();
        workoutDTO.setDate(LocalDateTime.now());
        workoutDTO.setDescription("Test workout");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> workoutService.createWorkout(workoutDTO));

        verify(userRepository, never()).findById(any());
        verify(workoutRepository, never()).save(any(Workout.class));
    }
}