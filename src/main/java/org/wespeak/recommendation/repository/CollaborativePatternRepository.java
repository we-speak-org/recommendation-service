package org.wespeak.recommendation.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.wespeak.recommendation.model.entity.CollaborativePattern;

public interface CollaborativePatternRepository
    extends MongoRepository<CollaborativePattern, String> {}
