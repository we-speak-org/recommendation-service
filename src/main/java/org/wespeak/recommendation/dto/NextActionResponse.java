package org.wespeak.recommendation.dto;

import java.util.Map;
import lombok.Builder;

@Builder
public record NextActionResponse(
    String action,
    Double confidence,
    ReasoningDto reasoning,
    NextActionRecommendationDto recommendation,
    NextActionRecommendationDto alternativeAction) {

  @Builder
  public record ReasoningDto(String primary, Map<String, Object> factors) {}

  @Builder
  public record NextActionRecommendationDto(
      String type, String topicCode, String lessonId, String titleKey, Integer estimatedDuration) {}
}
