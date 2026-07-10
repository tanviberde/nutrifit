package com.tanviberde.nutrifit.repository;

import com.tanviberde.nutrifit.entity.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    Page<Meal> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    List<Meal> findByUserIdAndMealDateAndDeletedFalse(Long userId, LocalDate mealDate);

    List<Meal> findByUserIdAndMealDateBetweenAndDeletedFalse(
            Long userId, LocalDate startDate, LocalDate endDate);
}