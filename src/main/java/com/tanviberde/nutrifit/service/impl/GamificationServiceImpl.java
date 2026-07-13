package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.entity.Achievement;
import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.entity.enums.AchievementType;
import com.tanviberde.nutrifit.dto.gamification.AchievementResponse;
import com.tanviberde.nutrifit.dto.gamification.StreakSummaryResponse;
import com.tanviberde.nutrifit.repository.AchievementRepository;
import com.tanviberde.nutrifit.repository.MealRepository;
import com.tanviberde.nutrifit.repository.UserRepository;
import com.tanviberde.nutrifit.repository.WorkoutRepository;
import com.tanviberde.nutrifit.service.ActivityTrackingService;
import com.tanviberde.nutrifit.service.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GamificationServiceImpl implements GamificationService {

    private static final int[] STREAK_MILESTONES = {3, 7, 14, 30, 60, 100};
    private static final long[] COUNT_MILESTONES = {1, 10, 50, 100};

    private final ActivityTrackingService activityTrackingService;
    private final AchievementRepository achievementRepository;
    private final MealRepository mealRepository;
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    @Override
    public void checkAndAwardAchievements(Long userId) {
        int dailyStreak = activityTrackingService.calculateCurrentStreak(userId);
        int workoutStreak = activityTrackingService.calculateWorkoutStreak(userId);
        int nutritionStreak = activityTrackingService.calculateNutritionStreak(userId);
        long mealCount = mealRepository.countByUserIdAndDeletedFalse(userId);
        long workoutCount = workoutRepository.countByUserIdAndDeletedFalse(userId);

        for (int milestone : STREAK_MILESTONES) {
            if (dailyStreak == milestone) {
                awardIfNotExists(userId, AchievementType.DAILY_STREAK,
                        milestone + "-Day Streak",
                        "Logged an activity for " + milestone + " consecutive days");
            }
            if (workoutStreak == milestone) {
                awardIfNotExists(userId, AchievementType.WORKOUT_STREAK,
                        milestone + "-Day Workout Streak",
                        "Logged a workout for " + milestone + " consecutive days");
            }
            if (nutritionStreak == milestone) {
                awardIfNotExists(userId, AchievementType.NUTRITION_STREAK,
                        milestone + "-Day Nutrition Streak",
                        "Logged a meal for " + milestone + " consecutive days");
            }
        }

        for (long milestone : COUNT_MILESTONES) {
            if (mealCount == milestone) {
                awardIfNotExists(userId, AchievementType.MILESTONE,
                        milestone + " Meals Logged",
                        "Logged " + milestone + " total meals");
            }
            if (workoutCount == milestone) {
                awardIfNotExists(userId, AchievementType.MILESTONE,
                        milestone + " Workouts Logged",
                        "Logged " + milestone + " total workouts");
            }
        }
    }

    @Override
    public List<AchievementResponse> getAchievements(Long userId) {
        return achievementRepository.findByUserIdAndDeletedFalse(userId).stream()
                .map(AchievementResponse::fromEntity)
                .toList();
    }

    @Override
    public StreakSummaryResponse getStreakSummary(Long userId) {
        return StreakSummaryResponse.builder()
                .dailyStreak(activityTrackingService.calculateCurrentStreak(userId))
                .workoutStreak(activityTrackingService.calculateWorkoutStreak(userId))
                .nutritionStreak(activityTrackingService.calculateNutritionStreak(userId))
                .build();
    }

    private void awardIfNotExists(Long userId, AchievementType type, String title, String description) {
        boolean alreadyEarned = achievementRepository
                .existsByUserIdAndAchievementTypeAndTitleAndDeletedFalse(userId, type, title);

        if (!alreadyEarned) {
            User userRef = userRepository.getReferenceById(userId);
            Achievement achievement = Achievement.builder()
                    .user(userRef)
                    .achievementType(type)
                    .title(title)
                    .description(description)
                    .earnedDate(LocalDate.now())
                    .build();
            achievementRepository.save(achievement);
        }
    }
}