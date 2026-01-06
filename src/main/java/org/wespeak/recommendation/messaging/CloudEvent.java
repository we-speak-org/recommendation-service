package org.wespeak.recommendation.messaging;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CloudEvent<T> {
  private String eventType;
  private String source;
  private String id;
  private Instant time;
  private T data;
  private String correlationId;
  private String tenantId;
}
