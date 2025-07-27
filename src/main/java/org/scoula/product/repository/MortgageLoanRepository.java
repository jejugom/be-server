package org.scoula.product.repository;

import java.util.Optional;

import org.scoula.product.domain.MortgageLoanDocuments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MortgageLoanRepository extends MongoRepository<MortgageLoanDocuments, String> {
	@Query("{ 'baseList.fin_prdt_cd': ?0 }")
	Optional<MortgageLoanDocuments> findByBaseListFinPrdtCd(String finPrdtCd);
}
