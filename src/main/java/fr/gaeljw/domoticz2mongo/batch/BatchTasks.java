package fr.gaeljw.domoticz2mongo.batch;

import fr.gaeljw.domoticz2mongo.batch.processor.DomoticzResultItemProcessor;
import fr.gaeljw.domoticz2mongo.mode.mongo.Temperature;
import fr.gaeljw.domoticz2mongo.model.domoticz.api.DomoticzResult;
import fr.gaeljw.domoticz2mongo.service.DomoticzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BatchTasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchTasks.class);
    private static final Logger MONGO_RETRY_LOGGER = LoggerFactory.getLogger("MONGO.RETRY");

    @Autowired
    private DomoticzService domoticzService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Scheduled(fixedDelayString = "${domoticz.query.interval}")
    public void importTemperatures() {
        LOGGER.info("Running importTemperaturesJob");

        List<DomoticzResult> results = domoticzService.getTemperatures();
        for (DomoticzResult r : results) {
            Temperature t = DomoticzResultItemProcessor.process(r);
            try {
                mongoTemplate.save(t, "temperatures");
            } catch (Exception e) {
                LOGGER.error("Exception lors de l'insertion", e);
                MONGO_RETRY_LOGGER.info("db.temperatures.insert({})", t.toMongoJson(false));
            }
        }

    }

}
