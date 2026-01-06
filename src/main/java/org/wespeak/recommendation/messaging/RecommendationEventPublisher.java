package org.wespeak.recommendation.messaging;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import org.wespeak.recommendation.model.entity.Recommendation;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationEventPublisher {

  private final StreamBridge streamBridge;

  public void publishGenerated(
      String userId, String targetLanguageCode, List<Recommendation> recs) {
    if (recs == null || recs.isEmpty()) {
      return;
    }
    RecommendationGeneratedPayload payload =
        new RecommendationGeneratedPayload(
            userId,
            targetLanguageCode,
            recs.stream()
                .map(
                    r ->
                        new RecommendationGeneratedPayload.RecommendationItem(
                            r.getId(),
                            r.getType().name(),
                            r.getTargetId(),
                            r.getTitle(),
                            r.getPriority()))
                .toList(),
            recs.size());

    CloudEvent<RecommendationGeneratedPayload> event =
        CloudEvent.<RecommendationGeneratedPayload>builder()
            .eventType("recommendation.generated")
            .source("recommendation-service")
            .id(UUID.randomUUID().toString())
            .time(Instant.now())
            .data(payload)
            .build();
    streamBridge.send("recommendationProducer-out-0", event);
    log.info("Published recommendation.generated for user={} count={}", userId, recs.size());
  }

  public void publishClicked(Recommendation rec) {
    RecommendationClickedPayload payload =
        new RecommendationClickedPayload(
            rec.getUserId(),
            rec.getId(),
            rec.getType().name(),
            rec.getTargetId(),
            rec.getTargetType().name(),
            rec.getClickedAt());

    CloudEvent<RecommendationClickedPayload> event =
        CloudEvent.<RecommendationClickedPayload>builder()
            .eventType("recommendation.clicked")
            .source("recommendation-service")
            .id(UUID.randomUUID().toString())
            .time(Instant.now())
            .data(payload)
            .build();
    streamBridge.send("recommendationProducer-out-0", event);
    log.info("Published recommendation.clicked id={}", rec.getId());
  }

  public record RecommendationGeneratedPayload(
      String userId,
      String targetLanguageCode,
      List<RecommendationItem> recommendations,
      int count) {
    public record RecommendationItem(
        String id, String type, String targetId, String title, Integer priority) {}
  }

  public record RecommendationClickedPayload(
      String userId,
      String recommendationId,
      String type,
      String targetId,
      String targetType,
      Instant clickedAt) {}
}
