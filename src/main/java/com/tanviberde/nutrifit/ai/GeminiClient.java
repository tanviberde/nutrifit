package com.tanviberde.nutrifit.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanviberde.nutrifit.exception.AiServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class GeminiClient {

    private final WebClient webClient;

    @Value("${app.ai.gemini.api-key}")
    private String apiKey;

    @Value("${app.ai.gemini.model}")
    private String model;

    public GeminiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String generateContent(String prompt) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(Map.of("text", prompt)))
                    )
            );

            JsonNode response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/models/{model}:generateContent")
                            .queryParam("key", apiKey)
                            .build(model))
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            return extractText(response);
        } catch (AiServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new AiServiceException("Failed to generate AI content: " + e.getMessage(), e);
        }
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