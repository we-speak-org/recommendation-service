package org.wespeak.recommendation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wespeak.recommendation.dto.LearningHistoryDto;
import org.wespeak.recommendation.dto.RecommendationsResponse;
import org.wespeak.recommendation.dto.UpdatePreferencesRequest;
import org.wespeak.recommendation.dto.UserPreferencesDto;
import org.wespeak.recommendation.service.RecommendationFacade;
import org.wespeak.recommendation.util.UserContext;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

  private final RecommendationFacade recommendationFacade;

  @GetMapping
  public ResponseEntity<RecommendationsResponse> getRecommendations(
      @RequestParam String language,
      @RequestParam(name = "limit", defaultValue = "5") Integer limit,
      @RequestParam(name = "type", required = false) String type,
      Authentication authentication) {
    String userId = UserContext.userId(authentication);
    return ResponseEntity.ok(
        recommendationFacade.getRecommendations(userId, language, limit, type));
  }

  @PostMapping("/{recommendationId}/click")
  public ResponseEntity<?> click(
      @PathVariable String recommendationId, Authentication authentication) {
    String userId = UserContext.userId(authentication);
    return ResponseEntity.ok(recommendationFacade.click(userId, recommendationId));
  }

  @PostMapping("/{recommendationId}/dismiss")
  public ResponseEntity<?> dismiss(
      @PathVariable String recommendationId, Authentication authentication) {
    String userId = UserContext.userId(authentication);
    return ResponseEntity.ok(recommendationFacade.dismiss(userId, recommendationId));
  }

  @GetMapping("/preferences")
  public ResponseEntity<UserPreferencesDto> getPreferences(
      @RequestParam String language, Authentication authentication) {
    String userId = UserContext.userId(authentication);
    return ResponseEntity.ok(recommendationFacade.getPreferences(userId, language));
  }

  @PutMapping("/preferences")
  public ResponseEntity<UserPreferencesDto> updatePreferences(
      @RequestParam String language,
      @Valid @RequestBody UpdatePreferencesRequest request,
      Authentication authentication) {
    String userId = UserContext.userId(authentication);
    return ResponseEntity.ok(recommendationFacade.updatePreferences(userId, language, request));
  }

  @GetMapping("/learning-history")
  public ResponseEntity<LearningHistoryDto> getLearningHistory(
      @RequestParam String language, Authentication authentication) {
    String userId = UserContext.userId(authentication);
    return ResponseEntity.ok(recommendationFacade.getLearningHistory(userId, language));
  }
}
