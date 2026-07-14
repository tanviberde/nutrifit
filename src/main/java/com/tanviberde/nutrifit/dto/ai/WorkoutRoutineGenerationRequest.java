package com.tanviberde.nutrifit.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutRoutineGenerationRequest {

    private Integer daysPerWeek;
    private String equipmentAccess;
}