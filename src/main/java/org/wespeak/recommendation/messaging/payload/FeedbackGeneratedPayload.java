package org.wespeak.recommendation.messaging.payload;

import java.util.List;

public record FeedbackGeneratedPayload(
    String feedbackId,
    String userId,
    String targetLanguageCode,
    Analysis analysis) {

  public record Analysis(List<FeedbackError> errors, List<FeedbackStrength> strengths) {}

  public record FeedbackError(String category, String subcategory, int count) {}

  public record FeedbackStrength(String category, String subcategory, int score) {}
}
