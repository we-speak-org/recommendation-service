package org.wespeak.recommendation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wespeak.recommendation.dto.FeedbackRequest;
import org.wespeak.recommendation.service.RecommendationService;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

  private final RecommendationService recommendationService;

  @GetMapping("/next-lesson")
  public ResponseEntity<?> getNextLesson(
      @RequestParam String targetLanguageCode, @RequestParam(required = false) String context) {
    return ResponseEntity.ok(
        recommendationService.getNextLesson(targetLanguageCode, context == null ? "" : context));
  }

  @GetMapping("/lessons")
  public ResponseEntity<?> getRecommendedLessons(@RequestParam String targetLanguageCode) {
    return ResponseEntity.ok(recommendationService.getRecommendedLessons(targetLanguageCode));
  }

  @GetMapping("/conversation-topics")
  public ResponseEntity<?> getConversationTopics(@RequestParam String targetLanguageCode) {
    return ResponseEntity.ok(recommendationService.getConversationTopics(targetLanguageCode));
  }

  @GetMapping("/next-action")
  public ResponseEntity<?> getNextAction(@RequestParam String targetLanguageCode) {
    return ResponseEntity.ok(recommendationService.getNextAction(targetLanguageCode));
  }

  @GetMapping("/learning-path")
  public ResponseEntity<?> getLearningPath(@RequestParam String targetLanguageCode) {
    return ResponseEntity.ok(recommendationService.getLearningPath(targetLanguageCode));
  }

  @GetMapping("/skill-gaps")
  public ResponseEntity<?> getSkillGaps(@RequestParam String targetLanguageCode) {
    return ResponseEntity.ok(recommendationService.getSkillGaps(targetLanguageCode));
  }

  @PostMapping("/feedback")
  public ResponseEntity<?> recordFeedback(@Valid @RequestBody FeedbackRequest request) {
    return ResponseEntity.ok(recommendationService.recordFeedback(request));
  }
}
