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

	private static final String MONGO_COLLECTION = "MONGO_COLLECTION";
	private static final String MONGO_USER = "MONGO_USER";
	private static final String MONGO_SECRET = "MONGO_PASSWORD";
	private static final String MONGO_SERVER_PRIMARY = "MONGO_SERVER_PRIMARY";
	private static final String MONGO_SERVER_SECONDARY_REPLICA = "MONGO_SERVER_SECONDARY_REPLICA";
	private static final String MONGO_SERVER_SECONDARY = "MONGO_SERVER_SECONDARY";
	private static final String MONGO_PORT = "MONGO_PORT";

	@Autowired
	private PropertiesReader properties;

	@Bean
	public MongoDbFactory mongoDbFactory() {
		StringBuilder sb = new StringBuilder();
		sb.append("mongodb://");
		sb.append(properties.getProperty(MONGO_USER));
		sb.append(":");
		sb.append(properties.getProperty(MONGO_SECRET));
		sb.append("@");
		sb.append(properties.getProperty(MONGO_SERVER_PRIMARY));
		sb.append(":");
		sb.append(properties.getProperty(MONGO_PORT));
		sb.append(",");
		sb.append(properties.getProperty(MONGO_SERVER_SECONDARY));
		sb.append(":");
		sb.append(properties.getProperty(MONGO_PORT));
		sb.append(",");
		sb.append(properties.getProperty(MONGO_SERVER_SECONDARY_REPLICA));
		sb.append(":");
		sb.append(properties.getProperty(MONGO_PORT));
		sb.append("/");
		sb.append(properties.getProperty(MONGO_COLLECTION));
		sb.append("?ssl=true&authSource=admin&retryWrites=true&w=majority");
		return new SimpleMongoClientDbFactory(sb.toString());
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoDbFactory());
	}
}
