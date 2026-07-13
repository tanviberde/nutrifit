package com.tanviberde.nutrifit.ai;

import com.tanviberde.nutrifit.dto.dashboard.DashboardResponse;
import com.tanviberde.nutrifit.dto.dashboard.PeriodSummaryResponse;
import com.tanviberde.nutrifit.dto.weight.WeightResponse;
import com.tanviberde.nutrifit.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromptBuilder {

    public String buildWeeklyReportPrompt(User user, PeriodSummaryResponse summary) {
        return """
                You are a supportive, knowledgeable fitness and nutrition coach writing a weekly \
                progress report for a client. Be encouraging but honest, and keep it to 3-4 short \
                paragraphs. Include specific, actionable suggestions for the coming week.

                Client goal: %s
                Diet preference: %s

                This week's data (%s to %s):
                - Total calories consumed: %.0f
                - Average daily calories: %.0f
                - Total protein: %.0fg
                - Total carbs: %.0fg
                - Total fat: %.0fg
                - Total fibre: %.0fg
                - Total calories burned through exercise: %.0f
                - Workouts completed: %d
                - Days with logged activity: %d out of 7
                - Current activity streak: %d days

                Write the weekly report now.
                """.formatted(
                user.getFitnessGoal(),
                user.getDietPreference(),
                summary.getStartDate(), summary.getEndDate(),
                summary.getTotalCalories(),
                summary.getAvgDailyCalories(),
                summary.getTotalProtein(),
                summary.getTotalCarbs(),
                summary.getTotalFat(),
                summary.getTotalFibre(),
                summary.getTotalCaloriesBurned(),
                summary.getWorkoutCount(),
                summary.getDaysLogged(),
                summary.getCurrentStreak()
        );
    }

    public String buildMotivationPrompt(User user, DashboardResponse dashboard) {
        return """
                You are an upbeat, genuine fitness coach. Write ONE short motivational message \
                (2-3 sentences max) for a client based on today's progress so far. Be specific to \
                their numbers, not generic. Do not use excessive exclamation points.

                Client goal: %s
                Current streak: %d days
                Calories consumed today: %.0f out of %.0f target
                Calories burned through exercise today: %.0f
                Workouts today: %d

                Write the motivational message now.
                """.formatted(
                user.getFitnessGoal(),
                dashboard.getCurrentStreak(),
                dashboard.getTotalCalories(),
                dashboard.getCalorieTarget() != null ? dashboard.getCalorieTarget() : 0.0,
                dashboard.getCaloriesBurned(),
                dashboard.getWorkoutCount()
        );
    }

    public String buildHabitAnalysisPrompt(
            User user, PeriodSummaryResponse monthSummary, List<WeightResponse> weightEntries) {

        String weightTrendData = weightEntries.isEmpty()
                ? "No weight entries logged this period."
                : weightEntries.stream()
                .map(w -> w.getEntryDate() + ": " + w.getWeightKg() + "kg")
                .reduce((a, b) -> a + ", " + b)
                .orElse("No weight entries logged this period.");

        return """
                You are a data-driven fitness analyst. Analyze the following 30-day habit data and \
                respond with ONLY a valid JSON object, no markdown formatting, no code fences, no \
                preamble text. The JSON must have exactly these string fields: calorieTrend, \
                proteinTrend, workoutConsistency, fibreTrend, weightTrend, overallInsight. Each \
                field should contain 1-2 sentences of specific, honest analysis.

                Client goal: %s

                30-day totals:
                - Total calories: %.0f (average %.0f/day)
                - Total protein: %.0fg
                - Total fibre: %.0fg
                - Workouts completed: %d
                - Days with logged activity: %d out of %d

                Weight entries over the period: %s

                Respond with the JSON object now.
                """.formatted(
                user.getFitnessGoal(),
                monthSummary.getTotalCalories(),
                monthSummary.getAvgDailyCalories(),
                monthSummary.getTotalProtein(),
                monthSummary.getTotalFibre(),
                monthSummary.getWorkoutCount(),
                monthSummary.getDaysLogged(),
                java.time.temporal.ChronoUnit.DAYS.between(
                        monthSummary.getStartDate(), monthSummary.getEndDate()) + 1,
                weightTrendData
        );
    }
}