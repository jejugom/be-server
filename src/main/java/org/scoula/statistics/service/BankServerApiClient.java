package org.scoula.statistics.service;

import java.util.List;

import org.scoula.statistics.dto.ProductClickStatsDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BankServerApiClient {

	private final RestTemplate restTemplate;

	private final String bankApiUrl = "http://localhost:8000/api/click-stats";

	public void sendClickStats(List<ProductClickStatsDto> stats) {
		restTemplate.postForEntity(bankApiUrl, stats, Void.class);
	}
}
