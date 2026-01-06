package org.wespeak.recommendation.model.entity;

import java.time.Instant;
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
@Document(collection = "recommendations")
@CompoundIndex(
    name = "user_state_idx",
    def = "{'userId':1,'targetLanguageCode':1,'dismissed':1,'expiresAt':1,'createdAt':-1}")
public class Recommendation {

  @Id private String id;
  private String userId;
  private String targetLanguageCode;

  private RecommendationType type;
  private String targetId;
  private TargetType targetType;

  private String title;
  private String reason;
  private Integer priority;
  private Map<String, Object> metadata;

  private Instant expiresAt;
  @Builder.Default private boolean dismissed = false;
  private Instant clickedAt;

  @CreatedDate private Instant createdAt;

  public enum RecommendationType {
    next_lesson,
    revision,
    conversation,
    practice
  }

  public enum TargetType {
    lesson,
    exercise,
    slot,
    unit
  }
}
