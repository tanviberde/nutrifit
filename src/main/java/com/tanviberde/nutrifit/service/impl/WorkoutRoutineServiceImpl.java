package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.ai.AiResponseParser;
import com.tanviberde.nutrifit.ai.GeminiClient;
import com.tanviberde.nutrifit.ai.GeneratedRoutineData;
import com.tanviberde.nutrifit.ai.PromptBuilder;
import com.tanviberde.nutrifit.dto.ai.WorkoutRoutineGenerationRequest;
import com.tanviberde.nutrifit.dto.routine.WorkoutRoutineResponse;
import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.entity.WorkoutRoutine;
import com.tanviberde.nutrifit.entity.WorkoutRoutineExercise;
import com.tanviberde.nutrifit.exception.ResourceNotFoundException;
import com.tanviberde.nutrifit.repository.UserRepository;
import com.tanviberde.nutrifit.repository.WorkoutRoutineExerciseRepository;
import com.tanviberde.nutrifit.repository.WorkoutRoutineRepository;
import com.tanviberde.nutrifit.service.WorkoutRoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutRoutineServiceImpl implements WorkoutRoutineService {

    private final UserRepository userRepository;
    private final WorkoutRoutineRepository workoutRoutineRepository;
    private final WorkoutRoutineExerciseRepository workoutRoutineExerciseRepository;
    private final PromptBuilder promptBuilder;
    private final GeminiClient geminiClient;
    private final AiResponseParser aiResponseParser;

    @Override
    public WorkoutRoutineResponse generateRoutine(Long userId, WorkoutRoutineGenerationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        int daysPerWeek = request.getDaysPerWeek() != null ? request.getDaysPerWeek() : 4;
        String equipmentAccess = request.getEquipmentAccess() != null
                ? request.getEquipmentAccess()
                : "Bodyweight and light dumbbells";

        String prompt = promptBuilder.buildWorkoutRoutinePrompt(user, daysPerWeek, equipmentAccess);
        String rawResponse = geminiClient.generateContent(prompt);
        GeneratedRoutineData data = aiResponseParser.parseWorkoutRoutine(rawResponse);

        WorkoutRoutine routine = WorkoutRoutine.builder()
                .user(user)
                .title(data.getTitle())
                .description(data.getDescription())
                .daysPerWeek(daysPerWeek)
                .build();

        WorkoutRoutine savedRoutine = workoutRoutineRepository.save(routine);

        List<WorkoutRoutineExercise> exercises = data.getExercises() == null
                ? List.of()
                : java.util.stream.IntStream.range(0, data.getExercises().size())
                .mapToObj(i -> {
                    GeneratedRoutineData.GeneratedExercise ge = data.getExercises().get(i);
                    return WorkoutRoutineExercise.builder()
                            .routine(savedRoutine)
                            .dayLabel(ge.getDayLabel())
                            .exerciseName(ge.getExerciseName())
                            .sets(ge.getSets())
                            .reps(ge.getReps())
                            .restSeconds(ge.getRestSeconds())
                            .orderIndex(i)
                            .build();
                })
                .toList();

        List<WorkoutRoutineExercise> savedExercises =
                workoutRoutineExerciseRepository.saveAll(exercises);

        return WorkoutRoutineResponse.fromEntity(savedRoutine, savedExercises);
    }

    @Override
    public Page<WorkoutRoutineResponse> getRoutines(Long userId, Pageable pageable) {
        return workoutRoutineRepository.findByUserIdAndDeletedFalse(userId, pageable)
                .map(routine -> {
                    List<WorkoutRoutineExercise> exercises = workoutRoutineExerciseRepository
                            .findByRoutineIdAndDeletedFalseOrderByOrderIndexAsc(routine.getId());
                    return WorkoutRoutineResponse.fromEntity(routine, exercises);
                });
    }
}