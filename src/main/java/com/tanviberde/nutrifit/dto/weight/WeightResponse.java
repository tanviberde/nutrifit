package com.tanviberde.nutrifit.dto.weight;

import com.tanviberde.nutrifit.entity.WeightEntry;
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
public class WeightResponse {

    private Long id;
    private Double weightKg;
    private LocalDate entryDate;

    public static WeightResponse fromEntity(WeightEntry entry) {
        return WeightResponse.builder()
                .id(entry.getId())
                .weightKg(entry.getWeightKg())
                .entryDate(entry.getEntryDate())
                .build();
    }
}