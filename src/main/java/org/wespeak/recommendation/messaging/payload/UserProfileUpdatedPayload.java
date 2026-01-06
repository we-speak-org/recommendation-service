package org.wespeak.recommendation.messaging.payload;

import java.util.List;

public record UserProfileUpdatedPayload(String userId, Changes changes) {
  public record Changes(List<LearningProfileChange> learningProfiles) {}

  public record LearningProfileChange(
      String targetLanguageCode, String currentLevel, Integer weeklyGoalMinutes) {}
}
