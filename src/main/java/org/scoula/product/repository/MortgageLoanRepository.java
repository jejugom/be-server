package org.scoula.product.repository;

import org.scoula.product.domain.MortgageLoanDocuments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MortgageLoanRepository extends MongoRepository<MortgageLoanDocuments, String> {
}
