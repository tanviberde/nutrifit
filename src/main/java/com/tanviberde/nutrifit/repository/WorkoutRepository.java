package com.tanviberde.nutrifit.repository;

import com.tanviberde.nutrifit.entity.Workout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    Page<Workout> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    List<Workout> findByUserIdAndWorkoutDateAndDeletedFalse(Long userId, LocalDate workoutDate);

    List<Workout> findByUserIdAndWorkoutDateBetweenAndDeletedFalse(
            Long userId, LocalDate startDate, LocalDate endDate);

    long countByUserIdAndDeletedFalse(Long userId);
}