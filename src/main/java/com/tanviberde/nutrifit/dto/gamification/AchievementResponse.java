package com.tanviberde.nutrifit.dto.gamification;

import com.tanviberde.nutrifit.entity.Achievement;
import com.tanviberde.nutrifit.entity.enums.AchievementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AchievementResponse {

    private Long id;
    private AchievementType achievementType;
    private String title;
    private String description;
    private LocalDate earnedDate;

    public static AchievementResponse fromEntity(Achievement achievement) {
        return AchievementResponse.builder()
                .id(achievement.getId())
                .achievementType(achievement.getAchievementType())
                .title(achievement.getTitle())
                .description(achievement.getDescription())
                .earnedDate(achievement.getEarnedDate())
                .build();
    }
}