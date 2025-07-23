package org.scoula.product.repository;

import org.scoula.product.domain.TimeDepositDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeDepositRepository extends MongoRepository<TimeDepositDocument, String> {
}
