package org.wespeak.recommendation.model.entity;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "recommendation_logs")
@CompoundIndex(name = "user_created_idx", def = "{'userId':1,'createdAt':-1}")
public class RecommendationLog {

  @Id private String id;
  private String userId;
  private String targetLanguageCode;
  private String recommendationType;
  private List<RecommendationEntry> recommendations;
  private ContextSnapshot contextSnapshot;
  private String userAction;
  private Instant actionTimestamp;

  @CreatedDate private Instant createdAt;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class RecommendationEntry {
    private String itemId;
    private Integer rank;
    private Double score;
    private Map<String, Double> scoringBreakdown;
    private String reason;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ContextSnapshot {
    private String currentLevel;
    private Integer skillGapsCount;
    private Integer daysSinceLastLesson;
  }
}
