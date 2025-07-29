package org.scoula.product.repository;

import java.util.Optional;

import org.scoula.product.domain.TimeDepositsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeDepositsRepository extends MongoRepository<TimeDepositsDocument, String> {
	@Query("{ 'baseList.fin_prdt_cd': ?0 }")
	Optional<TimeDepositsDocument> findByBaseListFinPrdtCd(String finPrdtCd);
}
