package org.scoula.product.repository;

import java.util.Optional;

import org.scoula.product.domain.SavingsDepositsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsDepositsRepository extends MongoRepository<SavingsDepositsDocument, String> {
	@Query("{ 'baseList.fin_prdt_cd': ?0 }")
	Optional<SavingsDepositsDocument> findByBaseListFinPrdtCd(String finPrdtCd);
}
