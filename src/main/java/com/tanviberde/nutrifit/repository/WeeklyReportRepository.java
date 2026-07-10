package com.tanviberde.nutrifit.repository;

import com.tanviberde.nutrifit.entity.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {

    Optional<WeeklyReport> findByUserIdAndWeekStartDateAndDeletedFalse(
            Long userId, LocalDate weekStartDate);

    List<WeeklyReport> findByUserIdAndDeletedFalseOrderByWeekStartDateDesc(Long userId);
}