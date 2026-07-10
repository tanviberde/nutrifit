package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.dto.workout.WorkoutRequest;
import com.tanviberde.nutrifit.dto.workout.WorkoutResponse;
import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.entity.Workout;
import com.tanviberde.nutrifit.exception.ResourceNotFoundException;
import com.tanviberde.nutrifit.exception.UnauthorizedException;
import com.tanviberde.nutrifit.repository.UserRepository;
import com.tanviberde.nutrifit.repository.WorkoutRepository;
import com.tanviberde.nutrifit.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

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
        return WorkoutResponse.fromEntity(savedWorkout);
    }

    @Override
    public WorkoutResponse updateWorkout(Long userId, Long workoutId, WorkoutRequest request) {
        Workout workout = getOwnedWorkout(userId, workoutId);

        workout.setWorkoutName(request.getWorkoutName());
        workout.setDurationMinutes(request.getDurationMinutes());
        workout.setCaloriesBurned(request.getCaloriesBurned());
        workout.setWorkoutDate(request.getWorkoutDate());

        Workout updatedWorkout = workoutRepository.save(workout);
        return WorkoutResponse.fromEntity(updatedWorkout);
    }

    @Override
    public void deleteWorkout(Long userId, Long workoutId) {
        Workout workout = getOwnedWorkout(userId, workoutId);
        workout.setDeleted(true);
        workoutRepository.save(workout);
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