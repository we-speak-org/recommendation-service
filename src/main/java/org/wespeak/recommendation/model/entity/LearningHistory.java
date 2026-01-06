package org.wespeak.recommendation.model.entity;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "learning_history")
@CompoundIndex(
    name = "user_lang_unique",
    def = "{'userId':1,'targetLanguageCode':1}",
    unique = true)
public class LearningHistory {
  @Id private String id;
  private String userId;
  private String targetLanguageCode;

  private List<String> completedLessonIds;
  private String lastLessonId;

  private List<WeakArea> weakAreas;
  private List<StrongArea> strongAreas;

  private Double averageScore;
  private Integer totalLessons;
  private Integer totalConversationMinutes;

  private Instant lastUpdated;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class WeakArea {
    private String category;
    private String subcategory;
    private Integer errorCount;
    private Instant lastErrorAt;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class StrongArea {
    private String category;
    private String subcategory;
    private Integer successRate;
  }
}
