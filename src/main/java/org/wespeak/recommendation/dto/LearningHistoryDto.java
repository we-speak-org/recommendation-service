package org.wespeak.recommendation.dto;

import java.time.Instant;
import java.util.List;
import lombok.Builder;

@Builder
public record LearningHistoryDto(
    String userId,
    String targetLanguageCode,
    Summary summary,
    List<WeakAreaDto> weakAreas,
    List<StrongAreaDto> strongAreas,
    RecentProgress recentProgress) {

  @Builder
  public record Summary(
      Integer totalLessons, Double averageScore, Integer totalConversationMinutes) {}

  @Builder
  public record WeakAreaDto(
      String category, String subcategory, Integer errorCount, Instant lastErrorAt) {}

  @Builder
  public record StrongAreaDto(String category, String subcategory, Integer successRate) {}

  @Builder
  public record RecentProgress(Integer lessonsLastWeek, Integer xpLastWeek, Integer streak) {}
}
