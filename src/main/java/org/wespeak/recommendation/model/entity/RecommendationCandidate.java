package org.wespeak.recommendation.model.entity;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "recommendation_candidates")
@CompoundIndexes({
  @CompoundIndex(name = "lang_level_idx", def = "{'targetLanguageCode':1, 'level':1}"),
  @CompoundIndex(name = "popularity_idx", def = "{'popularityScore':-1}")
})
public class RecommendationCandidate {

  @Id private String id;

  private String lessonId;
  private String targetLanguageCode;
  private String level;
  private String type;
  private String topicCode;

  private List<String> requiredSkills;
  private List<String> teachesSkills;
  private Integer difficultyScore;
  private Integer estimatedDuration;
  private Double avgCompletionRate;
  private Integer avgScore;
  private Double popularityScore;

  private Instant lastUpdated;
}
