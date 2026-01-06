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
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "learner_profiles")
@CompoundIndexes({
  @CompoundIndex(name = "user_lang_unique", def = "{'userId':1, 'targetLanguageCode':1}", unique = true),
  @CompoundIndex(name = "level_lang_idx", def = "{'currentLevel':1, 'targetLanguageCode':1}"),
  @CompoundIndex(name = "drop_off_idx", def = "{'engagement.dropOffRisk':-1}")
})
public class LearnerProfile {

  @Id private String id;
  private String userId;
  private String targetLanguageCode;
  private String nativeLanguageCode;

  private String currentLevel;
  private String assessedLevel;
  private String goal; // work, travel, studies, personal

  private Map<String, SkillMastery> skillsMastery;
  private List<RecurringError> recurringErrors;

  private List<String> completedLessons;
  private List<ConversationTopicHistory> conversationTopics;

  private Engagement engagement;
  private Preferences preferences;
  private Features features;

  @LastModifiedDate private Instant lastUpdated;
  @CreatedDate private Instant createdAt;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SkillMastery {
    private Integer masteryLevel;
    private Instant lastPracticed;
    private Integer encountersCount;
    private Double correctRate;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class RecurringError {
    private String category;
    private Integer frequency;
    private String severity;
    private Instant lastOccurrence;
    private String relatedSkillId;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ConversationTopicHistory {
    private String topicCode;
    private Integer completedCount;
    private Integer averageScore;
    private Instant lastCompleted;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Engagement {
    private String preferredTimeOfDay;
    private Integer averageSessionDuration;
    private Integer lessonsPerWeek;
    private Integer conversationsPerWeek;
    private Integer currentStreak;
    private Double dropOffRisk;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Preferences {
    private Map<String, Double> contentTypes;
    private String difficultyPreference;
    private String pacePreference;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Features {
    private Double learningVelocity;
    private Double retentionRate;
    private Double consistencyScore;
    private Double socialEngagement;
  }
}
