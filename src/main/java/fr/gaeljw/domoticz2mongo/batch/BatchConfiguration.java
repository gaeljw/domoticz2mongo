package fr.gaeljw.domoticz2mongo.batch;

import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.UnknownHostException;

@Configuration
@EnableScheduling
public class BatchConfiguration {

    @Value("${mongo.client.uri}")
    private String mongoClientUri;

    @Bean
    public MongoTemplate mongoTemplate() throws UnknownHostException {
        return new MongoTemplate(mongoDbFactory());
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(mongoClientUri));
    }

}
