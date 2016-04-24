package fr.gaeljw.domoticz2mongo.batch;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClientURI;
import fr.gaeljw.domoticz2mongo.batch.processor.DomoticzResultItemProcessor;
import fr.gaeljw.domoticz2mongo.mode.mongo.Temperature;
import fr.gaeljw.domoticz2mongo.model.domoticz.api.DomoticzResult;
import fr.gaeljw.domoticz2mongo.service.DomoticzService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.UnknownHostException;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DomoticzService domoticzService;

    @Value("${mongo.client.uri}")
    private String mongoClientUri;

    @Bean
    @StepScope
    public ItemReader<DomoticzResult> reader() {
        return new ListItemReader<>(domoticzService.getTemperatures());
    }

    @Bean
    public ItemProcessor<DomoticzResult, Temperature> processor() {
        return new DomoticzResultItemProcessor();
    }

    @Bean
    public ItemWriter<Temperature> writer() throws UnknownHostException {
        MongoItemWriter<Temperature> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate());
        writer.setCollection("temperatures");
        return writer;
    }

    @Bean
    public Job importTemperaturesJob() throws UnknownHostException {
        return jobBuilderFactory.get("importTemperaturesJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() throws UnknownHostException {
        return stepBuilderFactory.get("step1")
                .allowStartIfComplete(true)
                .<DomoticzResult, Temperature>chunk(1) // commit each item
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public MongoTemplate mongoTemplate() throws UnknownHostException {
        return new MongoTemplate(mongoDbFactory());
    }

//    @Bean
//    public Mongo mongo() throws UnknownHostException {
//        return new Mongo("localhost");
//    }

//    @Bean
//    public MongoClientFactoryBean mongo() {
//        MongoClientFactoryBean factory = new MongoClientFactoryBean();
//        factory.setHost("localhost");
//        return factory;
//    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(mongoClientUri));
    }

}
