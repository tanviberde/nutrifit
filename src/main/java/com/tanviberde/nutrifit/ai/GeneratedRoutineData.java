package com.tanviberde.nutrifit.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedRoutineData {

    private String title;
    private String description;
    private List<GeneratedExercise> exercises;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeneratedExercise {
        private String dayLabel;
        private String exerciseName;
        private Integer sets;
        private String reps;
        private Integer restSeconds;
    }
}