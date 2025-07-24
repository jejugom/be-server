package org.scoula.product.repository;

import org.scoula.product.domain.TimeDepositsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeDepositsRepository extends MongoRepository<TimeDepositsDocument, String> {
}
