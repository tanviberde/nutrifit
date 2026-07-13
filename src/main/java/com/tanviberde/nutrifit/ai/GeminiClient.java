package com.tanviberde.nutrifit.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanviberde.nutrifit.exception.AiServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class GeminiClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.ai.gemini.api-key}")
    private String apiKey;

    @Value("${app.ai.gemini.model}")
    private String model;

    public GeminiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String generateContent(String prompt) {
        int maxAttempts = 3;
        long delayMs = 2000;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                Map<String, Object> requestBody = Map.of(
                        "contents", List.of(
                                Map.of("parts", List.of(Map.of("text", prompt)))
                        )
                );

                String rawResponse = webClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path("/models/{model}:generateContent")
                                .queryParam("key", apiKey)
                                .build(model))
                        .bodyValue(requestBody)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                JsonNode response = objectMapper.readTree(rawResponse);
                return extractText(response);
            } catch (Exception e) {
                boolean isLastAttempt = attempt == maxAttempts;
                if (isLastAttempt) {
                    throw new AiServiceException(
                            "Failed to generate AI content after " + maxAttempts + " attempts: " + e.getMessage(), e);
                }
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                delayMs *= 2;
            }
        }

        throw new AiServiceException("Failed to generate AI content: exhausted retries");
    }

    private String extractText(JsonNode response) {
        if (response == null) {
            throw new AiServiceException("Received empty response from AI service");
        }

        JsonNode textNode = response
                .path("candidates").path(0)
                .path("content").path("parts").path(0)
                .path("text");

        if (textNode.isMissingNode() || textNode.asText().isBlank()) {
            throw new AiServiceException("AI response did not contain usable content");
        }

        return textNode.asText();
    }
}