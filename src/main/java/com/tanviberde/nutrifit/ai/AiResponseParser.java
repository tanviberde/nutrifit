package com.tanviberde.nutrifit.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanviberde.nutrifit.dto.ai.HabitAnalysisResponse;
import com.tanviberde.nutrifit.exception.AiServiceException;
import org.springframework.stereotype.Component;

@Component
public class AiResponseParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public HabitAnalysisResponse parseHabitAnalysis(String rawResponse) {
        try {
            String cleaned = stripMarkdownFences(rawResponse);
            return objectMapper.readValue(cleaned, HabitAnalysisResponse.class);
        } catch (Exception e) {
            throw new AiServiceException("Failed to parse AI habit analysis response: " + e.getMessage(), e);
        }
    }

    public GeneratedRecipeData parseRecipeGeneration(String rawResponse) {
        try {
            String cleaned = stripMarkdownFences(rawResponse);
            return objectMapper.readValue(cleaned, GeneratedRecipeData.class);
        } catch (Exception e) {
            throw new AiServiceException("Failed to parse AI recipe response: " + e.getMessage(), e);
        }
    }

    private String stripMarkdownFences(String text) {
        String trimmed = text.trim();
        if (trimmed.startsWith("```")) {
            trimmed = trimmed.replaceFirst("^```(json)?", "");
            trimmed = trimmed.replaceFirst("```$", "");
        }
        return trimmed.trim();
    }
}