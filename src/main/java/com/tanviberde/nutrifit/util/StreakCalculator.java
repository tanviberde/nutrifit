package com.tanviberde.nutrifit.util;

import java.time.LocalDate;
import java.util.Set;

public final class StreakCalculator {

    private StreakCalculator() {
    }

    public static int calculateStreak(Set<LocalDate> activeDates, LocalDate referenceDate) {
        int streak = 0;
        LocalDate cursor = referenceDate;

        if (!activeDates.contains(cursor)) {
            cursor = cursor.minusDays(1);
        }

        while (activeDates.contains(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }

        return streak;
    }
}