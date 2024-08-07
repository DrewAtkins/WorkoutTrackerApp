package com.example.workouttracker.repository;

import com.example.workouttracker.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserIdOrderByDateDesc(Long userId);
}