package com.tanviberde.nutrifit.repository;

import com.tanviberde.nutrifit.entity.WorkoutRoutine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRoutineRepository extends JpaRepository<WorkoutRoutine, Long> {

    Page<WorkoutRoutine> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);
}