package org.wespeak.recommendation.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record LearningPathResponse(
    String currentPhase,
    Double phaseProgress,
    List<MilestoneDto> upcomingMilestones,
    List<WeeklyPlanDto> recommendedPath,
    PredictedOutcomeDto predictedOutcome) {

  @Builder
  public record MilestoneDto(String milestone, Integer lessonsRemaining, Integer estimatedDays) {}

  @Builder
  public record WeeklyPlanDto(Integer week, List<PlannedActivityDto> activities) {}

  @Builder
  public record PlannedActivityDto(
      String day, String type, String itemId, String topicCode, Integer duration) {}

  @Builder
  public record PredictedOutcomeDto(
      String estimatedLevelIn30Days, Integer estimatedXPGain, List<String> skillsToMaster) {}
}
