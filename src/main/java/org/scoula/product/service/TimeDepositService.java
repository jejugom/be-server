package org.scoula.product.service;

import java.util.List;

import org.scoula.product.domain.TimeDepositDocument;
import org.scoula.product.repository.TimeDepositRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class TimeDepositService {
	private final TimeDepositRepository repository;

	public TimeDepositService(TimeDepositRepository repository) {
		this.repository = repository;
	}

	public List<TimeDepositDocument> findAll() {
		return repository.findAll();
	}
}
