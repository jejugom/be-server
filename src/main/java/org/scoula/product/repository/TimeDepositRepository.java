package org.scoula.product.repository;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.product.domain.TimeDepositDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Mapper
public interface TimeDepositRepository extends MongoRepository<TimeDepositDocument, String> {
}
