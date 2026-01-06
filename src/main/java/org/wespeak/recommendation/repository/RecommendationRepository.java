package org.wespeak.recommendation.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.wespeak.recommendation.model.entity.Recommendation;

public interface RecommendationRepository extends MongoRepository<Recommendation, String> {

  @Query(
      "{ 'userId': ?0, 'targetLanguageCode': ?1, 'dismissed': false, $or: [ {'expiresAt': null }, {'expiresAt': { $gt: ?2 }} ] }")
  List<Recommendation> findActive(String userId, String targetLanguageCode, Instant now);

  Optional<Recommendation> findByIdAndUserId(String id, String userId);
}
