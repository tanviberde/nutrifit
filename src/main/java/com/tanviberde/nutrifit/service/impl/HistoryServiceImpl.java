package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.dto.history.HistoryFilterRequest;
import com.tanviberde.nutrifit.dto.meal.MealResponse;
import com.tanviberde.nutrifit.dto.weight.WeightResponse;
import com.tanviberde.nutrifit.dto.workout.WorkoutResponse;
import com.tanviberde.nutrifit.entity.Meal;
import com.tanviberde.nutrifit.entity.Workout;
import com.tanviberde.nutrifit.repository.MealRepository;
import com.tanviberde.nutrifit.repository.WeightEntryRepository;
import com.tanviberde.nutrifit.repository.WorkoutRepository;
import com.tanviberde.nutrifit.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private static final LocalDate DEFAULT_START = LocalDate.of(2000, 1, 1);

    private final MealRepository mealRepository;
    private final WorkoutRepository workoutRepository;
    private final WeightEntryRepository weightEntryRepository;

    @Override
    public Page<MealResponse> getMealHistory(Long userId, HistoryFilterRequest filter, Pageable pageable) {
        LocalDate start = filter.getStartDate() != null ? filter.getStartDate() : DEFAULT_START;
        LocalDate end = filter.getEndDate() != null ? filter.getEndDate() : LocalDate.now();

        List<Meal> meals = mealRepository
                .findByUserIdAndMealDateBetweenAndDeletedFalse(userId, start, end);

        List<MealResponse> filtered = meals.stream()
                .filter(m -> matchesKeyword(m.getMealName(), filter.getSearchKeyword()))
                .map(MealResponse::fromEntity)
                .toList();

        return paginate(filtered, pageable);
    }

    @Override
    public Page<WorkoutResponse> getWorkoutHistory(
            Long userId, HistoryFilterRequest filter, Pageable pageable) {
        LocalDate start = filter.getStartDate() != null ? filter.getStartDate() : DEFAULT_START;
        LocalDate end = filter.getEndDate() != null ? filter.getEndDate() : LocalDate.now();

        List<Workout> workouts = workoutRepository
                .findByUserIdAndWorkoutDateBetweenAndDeletedFalse(userId, start, end);

        List<WorkoutResponse> filtered = workouts.stream()
                .filter(w -> matchesKeyword(w.getWorkoutName(), filter.getSearchKeyword()))
                .map(WorkoutResponse::fromEntity)
                .toList();

        return paginate(filtered, pageable);
    }

    @Override
    public Page<WeightResponse> getWeightHistory(
            Long userId, HistoryFilterRequest filter, Pageable pageable) {
        LocalDate start = filter.getStartDate() != null ? filter.getStartDate() : DEFAULT_START;
        LocalDate end = filter.getEndDate() != null ? filter.getEndDate() : LocalDate.now();

        return weightEntryRepository
                .findByUserIdAndEntryDateBetweenAndDeletedFalse(userId, start, end, pageable)
                .map(WeightResponse::fromEntity);
    }

    private boolean matchesKeyword(String name, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }
        return name != null && name.toLowerCase().contains(keyword.toLowerCase());
    }

    private <T> Page<T> paginate(List<T> fullList, Pageable pageable) {
        int start = (int) pageable.getOffset();
        if (start >= fullList.size()) {
            return new PageImpl<>(List.of(), pageable, fullList.size());
        }
        int end = Math.min(start + pageable.getPageSize(), fullList.size());
        return new PageImpl<>(fullList.subList(start, end), pageable, fullList.size());
    }
}