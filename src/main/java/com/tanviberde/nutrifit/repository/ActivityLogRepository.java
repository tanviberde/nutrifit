package com.tanviberde.nutrifit.repository;

import com.tanviberde.nutrifit.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    Optional<ActivityLog> findByUserIdAndActivityDateAndDeletedFalse(
            Long userId, LocalDate activityDate);

    List<ActivityLog> findByUserIdAndActivityDateBetweenAndDeletedFalseOrderByActivityDateDesc(
            Long userId, LocalDate startDate, LocalDate endDate);
}