package com.tanviberde.nutrifit.repository;

import com.tanviberde.nutrifit.entity.WorkoutRoutineExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRoutineExerciseRepository extends JpaRepository<WorkoutRoutineExercise, Long> {

    List<WorkoutRoutineExercise> findByRoutineIdAndDeletedFalseOrderByOrderIndexAsc(Long routineId);
}