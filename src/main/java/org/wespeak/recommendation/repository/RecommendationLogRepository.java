package org.wespeak.recommendation.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.wespeak.recommendation.model.entity.RecommendationLog;

public interface RecommendationLogRepository extends MongoRepository<RecommendationLog, String> {}
