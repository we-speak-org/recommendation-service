package org.wespeak.recommendation.model.entity;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_preferences")
@CompoundIndex(
    name = "user_lang_unique",
    def = "{'userId':1,'targetLanguageCode':1}",
    unique = true)
public class UserPreferences {
  @Id private String id;
  private String userId;
  private String targetLanguageCode;

  private List<String> preferredLearningTime;
  @Builder.Default private Integer dailyGoalMinutes = 15;
  private List<String> focusAreas;
  private List<String> excludedTopics;

  @CreatedDate private Instant createdAt;
  @LastModifiedDate private Instant updatedAt;
}
