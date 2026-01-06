package org.wespeak.recommendation.client;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
public class ConversationServiceClient {

  @Value("${services.conversation.base-url:http://conversation-service}")
  private String baseUrl;

  private final RestTemplate restTemplate;

  public List<ConversationSlot> getAvailableSlots(String language, String level) {
    Instant now = Instant.now();
    Instant to = now.plus(24, ChronoUnit.HOURS);
    String url =
        "%s/api/v1/slots/available?language=%s&level=%s&from=%s&to=%s"
            .formatted(baseUrl, language, level, now, to);
    try {
      ResponseEntity<ConversationSlotsResponse> resp =
          restTemplate.exchange(url, HttpMethod.GET, null, ConversationSlotsResponse.class);
      return resp.getBody() != null ? resp.getBody().slots() : Collections.emptyList();
    } catch (RestClientException ex) {
      log.warn("Failed to fetch conversation slots {}", ex.getMessage());
      return Collections.emptyList();
    }
  }

  public record ConversationSlotsResponse(List<ConversationSlot> slots) {}

  public record ConversationSlot(
      String id,
      String language,
      String level,
      Instant startTime,
      int participantsCount,
      int maxParticipants) {}
}
