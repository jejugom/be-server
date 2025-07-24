package org.scoula.product.repository;

import org.scoula.product.domain.SavingsDepositsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsDepositsRepository extends MongoRepository<SavingsDepositsDocument, String> {
}
