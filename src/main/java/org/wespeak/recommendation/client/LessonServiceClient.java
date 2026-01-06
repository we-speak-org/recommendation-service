package org.wespeak.recommendation.client;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class LessonServiceClient {

  @Value("${services.lesson.base-url:http://lesson-service}")
  private String baseUrl;

  private final RestTemplate restTemplate;

  public ProgressResponse getProgress(String userId, String language) {
    String url =
        "%s/api/v1/lessons/progress?userId=%s&language=%s".formatted(baseUrl, userId, language);
    try {
      ResponseEntity<ProgressResponse> resp =
          restTemplate.exchange(url, HttpMethod.GET, null, ProgressResponse.class);
      return resp.getBody();
    } catch (RestClientException ex) {
      log.warn("Failed to fetch lesson progress {}", ex.getMessage());
      return null;
    }
  }

  public RevisionExercise findRevisionExercise(String language, String category, String subcategory) {
    String url =
        "%s/api/v1/lessons/revision?language=%s&category=%s&subcategory=%s"
            .formatted(baseUrl, language, category, subcategory);
    try {
      ResponseEntity<RevisionExercise> resp =
          restTemplate.exchange(url, HttpMethod.GET, null, RevisionExercise.class);
      return resp.getBody();
    } catch (RestClientException ex) {
      log.warn("Failed to fetch revision exercise {}", ex.getMessage());
      return null;
    }
  }

  public record ProgressResponse(
      String courseId,
      List<String> completedLessonIds,
      Lesson nextLesson,
      String currentUnitId,
      boolean unitUnlocked) {}

  public record Lesson(
      String id, String title, String unitName, Integer estimatedMinutes, boolean prerequisitesMet) {}

  public record RevisionExercise(String id, String title, Integer estimatedMinutes) {}
}
