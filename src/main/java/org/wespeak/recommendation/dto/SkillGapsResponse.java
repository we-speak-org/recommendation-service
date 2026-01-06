package org.wespeak.recommendation.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record SkillGapsResponse(
    List<IdentifiedGapDto> identifiedGaps,
    List<StrengthAreaDto> strengthAreas,
    SummaryDto summary) {

  @Builder
  public record IdentifiedGapDto(
      String skillId,
      String skillCode,
      String nameKey,
      String category,
      Integer priority,
      GapEvidenceDto evidence,
      List<RecommendedActionDto> recommendedActions) {}

  @Builder
  public record GapEvidenceDto(Integer errorCount, String lastError, Integer relatedLessonsNotCompleted) {}

  @Builder
  public record RecommendedActionDto(String type, String itemId, String titleKey) {}

  @Builder
  public record StrengthAreaDto(String skillCode, Integer masteryLevel, String lastPracticed) {}

  @Builder
  public record SummaryDto(Integer totalGaps, Integer highPriorityGaps, Integer strengthsCount, Double overallReadiness) {}
}
