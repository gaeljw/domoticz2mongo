package fr.gaeljw.domoticz2mongo.batch.processor;

import fr.gaeljw.domoticz2mongo.mode.mongo.Temperature;
import fr.gaeljw.domoticz2mongo.model.domoticz.api.DomoticzResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.ZonedDateTime;
import java.util.Date;

// Could use JSR 352 instead of Spring for batch...
public class DomoticzResultItemProcessor implements ItemProcessor<DomoticzResult, Temperature> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomoticzResultItemProcessor.class);

    @Override
    public Temperature process(DomoticzResult domoticzResult) throws Exception {
        LOGGER.info("Processing temperature for device id {}", domoticzResult.getIdx());
        Temperature t = new Temperature();
        t.setIdDevice(domoticzResult.getIdx());
        t.setNameDevice(domoticzResult.getName());
        t.setDateTime(new Date()); // TODO or use lastUpdate from result if newer
        t.setTemperature(domoticzResult.getTemp());
        return t;
    }
}
