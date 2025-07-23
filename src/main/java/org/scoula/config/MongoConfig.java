package org.scoula.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = "org.scoula.product.repository")
public class MongoConfig {

	@Value("${mongodb.uri}")
	private String mongoUri;

	@Value("${mongodb.database}")
	private String mongoDatabase;

	@Bean
	public MongoClient mongoClient() {
		return MongoClients.create(mongoUri);
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), mongoDatabase);
	}
}
