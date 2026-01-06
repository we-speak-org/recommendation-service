package org.wespeak.recommendation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.Data;

@Data
public class UpdatePreferencesRequest {
  private List<String> preferredLearningTime;

  @Min(5)
  @Max(120)
  private Integer dailyGoalMinutes;

  private List<String> focusAreas;
  private List<String> excludedTopics;
}
