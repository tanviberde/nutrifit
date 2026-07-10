package com.tanviberde.nutrifit.repository;

import com.tanviberde.nutrifit.entity.DailyProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyProgressRepository extends JpaRepository<DailyProgress, Long> {

    Optional<DailyProgress> findByUserIdAndProgressDateAndDeletedFalse(
            Long userId, LocalDate progressDate);

    List<DailyProgress> findByUserIdAndProgressDateBetweenAndDeletedFalse(
            Long userId, LocalDate startDate, LocalDate endDate);
}