package org.wespeak.recommendation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedbackRequest {
  @NotBlank private String recommendationId;
  @NotBlank private String action;
  @NotBlank private String itemId;
  @NotNull private String timestamp;
  private FeedbackContext context;

  @Data
  public static class FeedbackContext {
    private Integer position;
    private Integer alternatives;
  }
}
