package com.tanviberde.nutrifit.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tanviberde.nutrifit.entity.WeightEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeightEntryRepository extends JpaRepository<WeightEntry, Long> {

    List<WeightEntry> findByUserIdAndDeletedFalse(Long userId);

    List<WeightEntry> findByUserIdAndEntryDateBetweenAndDeletedFalse(
            Long userId, LocalDate startDate, LocalDate endDate);

    Optional<WeightEntry> findTopByUserIdAndDeletedFalseOrderByEntryDateDesc(Long userId);

    Page<WeightEntry> findByUserIdAndEntryDateBetweenAndDeletedFalse(
            Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}