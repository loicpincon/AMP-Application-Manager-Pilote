package application.manager.pilote.commun.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

import application.manager.pilote.commun.helper.PropertiesReader;

@Configuration
public class MongoDbConfig {

	@Autowired
	private PropertiesReader properties;

	@Bean
	public MongoDbFactory mongoDbFactory() {
		StringBuilder sb = new StringBuilder();
		sb.append("mongodb://");
		sb.append(properties.getProperty("MONGO_USER"));
		sb.append(":");
		sb.append(properties.getProperty("MONGO_PASSWORD"));
		sb.append("@");
		sb.append(properties.getProperty("MONGO_SERVER_PRIMARY"));
		sb.append(":");
		sb.append(properties.getProperty("MONGO_PORT"));
		sb.append(",");
		sb.append(properties.getProperty("MONGO_SERVER_SECONDARY"));
		sb.append(":");
		sb.append(properties.getProperty("MONGO_PORT"));
		sb.append(",");
		sb.append(properties.getProperty("MONGO_SERVER_SECONDARY_REPLICA"));
		sb.append(":");
		sb.append(properties.getProperty("MONGO_PORT"));
		sb.append("/");
		sb.append(properties.getProperty("MONGO_COLLECTION"));
		sb.append("?ssl=true&replicaSet=mobilite-shard-0&authSource=admin&retryWrites=true&w=majority");
		return new SimpleMongoClientDbFactory(sb.toString());
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		return mongoTemplate;
	}
}
