package fr.gaeljw.domoticz2mongo.batch.processor;

import fr.gaeljw.domoticz2mongo.mode.mongo.Temperature;
import fr.gaeljw.domoticz2mongo.model.domoticz.api.DomoticzResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class DomoticzResultItemProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomoticzResultItemProcessor.class);

    public static Temperature process(DomoticzResult domoticzResult) {
        LOGGER.info("Processing temperature for device id {}", domoticzResult.getIdx());
        Temperature t = new Temperature();
        t.setIdDevice(domoticzResult.getIdx());
        t.setNameDevice(domoticzResult.getName());
        t.setDateTime(new Date()); // TODO or use lastUpdate from result if newer
        t.setTemperature(domoticzResult.getTemp());
        return t;
    }
}
