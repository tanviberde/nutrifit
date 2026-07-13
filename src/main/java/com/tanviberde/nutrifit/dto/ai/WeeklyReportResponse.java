package com.tanviberde.nutrifit.dto.ai;

import com.tanviberde.nutrifit.entity.WeeklyReport;
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
public class WeeklyReportResponse {

    private Long id;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private String reportContent;

    public static WeeklyReportResponse fromEntity(WeeklyReport report) {
        return WeeklyReportResponse.builder()
                .id(report.getId())
                .weekStartDate(report.getWeekStartDate())
                .weekEndDate(report.getWeekEndDate())
                .reportContent(report.getReportContent())
                .build();
    }
}