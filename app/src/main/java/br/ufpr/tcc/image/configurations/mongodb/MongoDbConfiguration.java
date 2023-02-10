package br.ufpr.tcc.image.configurations.mongodb;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = {"br.ufpr.tcc.image.gateways.outputs.mongodb"})
public class MongoDbConfiguration {}
