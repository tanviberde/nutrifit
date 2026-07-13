package com.tanviberde.nutrifit.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitAnalysisResponse {

    private String calorieTrend;
    private String proteinTrend;
    private String workoutConsistency;
    private String fibreTrend;
    private String weightTrend;
    private String overallInsight;
}