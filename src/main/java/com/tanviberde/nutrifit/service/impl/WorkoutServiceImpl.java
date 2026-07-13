package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.dto.workout.WorkoutRequest;
import com.tanviberde.nutrifit.dto.workout.WorkoutResponse;
import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.entity.Workout;
import com.tanviberde.nutrifit.exception.ResourceNotFoundException;
import com.tanviberde.nutrifit.exception.UnauthorizedException;
import com.tanviberde.nutrifit.repository.UserRepository;
import com.tanviberde.nutrifit.repository.WorkoutRepository;
import com.tanviberde.nutrifit.service.ActivityTrackingService;
import com.tanviberde.nutrifit.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.tanviberde.nutrifit.service.GamificationService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ActivityTrackingService activityTrackingService;
    private final GamificationService gamificationService;

    @Override
    public WorkoutResponse createWorkout(Long userId, WorkoutRequest request) {
        User userRef = userRepository.getReferenceById(userId);

        Workout workout = Workout.builder()
                .user(userRef)
                .workoutName(request.getWorkoutName())
                .durationMinutes(request.getDurationMinutes())
                .caloriesBurned(request.getCaloriesBurned())
                .workoutDate(request.getWorkoutDate())
                .build();

        Workout savedWorkout = workoutRepository.save(workout);

        activityTrackingService.recalculateDailyProgress(userId, savedWorkout.getWorkoutDate());
        activityTrackingService.logWorkoutActivity(userId, savedWorkout.getWorkoutDate());
        gamificationService.checkAndAwardAchievements(userId);

        return WorkoutResponse.fromEntity(savedWorkout);
    }

    @Override
    public WorkoutResponse updateWorkout(Long userId, Long workoutId, WorkoutRequest request) {
        Workout workout = getOwnedWorkout(userId, workoutId);
        LocalDate oldDate = workout.getWorkoutDate();

        workout.setWorkoutName(request.getWorkoutName());
        workout.setDurationMinutes(request.getDurationMinutes());
        workout.setCaloriesBurned(request.getCaloriesBurned());
        workout.setWorkoutDate(request.getWorkoutDate());

        Workout updatedWorkout = workoutRepository.save(workout);

        activityTrackingService.recalculateDailyProgress(userId, oldDate);
        if (!oldDate.equals(updatedWorkout.getWorkoutDate())) {
            activityTrackingService.recalculateDailyProgress(userId, updatedWorkout.getWorkoutDate());
        }
        activityTrackingService.logWorkoutActivity(userId, updatedWorkout.getWorkoutDate());

        return WorkoutResponse.fromEntity(updatedWorkout);
    }

    @Override
    public void deleteWorkout(Long userId, Long workoutId) {
        Workout workout = getOwnedWorkout(userId, workoutId);
        LocalDate workoutDate = workout.getWorkoutDate();

        workout.setDeleted(true);
        workoutRepository.save(workout);

        activityTrackingService.recalculateDailyProgress(userId, workoutDate);
    }

    @Override
    public Page<WorkoutResponse> getWorkouts(Long userId, Pageable pageable) {
        return workoutRepository.findByUserIdAndDeletedFalse(userId, pageable)
                .map(WorkoutResponse::fromEntity);
    }

    private Workout getOwnedWorkout(Long userId, Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found"));

        if (!workout.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You do not have permission to access this workout");
        }

        return workout;
    }
}