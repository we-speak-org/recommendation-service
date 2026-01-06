package org.wespeak.recommendation.model.entity;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "collaborative_patterns")
public class CollaborativePattern {

  @Id private String id;
  private ProfileSignature profileSignature;
  private List<SuccessfulPath> successfulPaths;
  private List<PopularTopic> popularTopics;
  private Instant lastUpdated;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ProfileSignature {
    private String level;
    private String nativeLanguage;
    private String targetLanguage;
    private String goal;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SuccessfulPath {
    private List<String> lessonSequence;
    private Double avgSuccessRate;
    private Integer usersFollowed;
    private Integer avgTimeToCompleteMinutes;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class PopularTopic {
    private String topicCode;
    private Double engagementScore;
    private Integer avgImprovement;
  }
}
