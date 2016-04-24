package fr.gaeljw.domoticz2mongo.service;

import fr.gaeljw.domoticz2mongo.model.domoticz.api.DomoticzResponse;
import fr.gaeljw.domoticz2mongo.model.domoticz.api.DomoticzResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.util.*;

@Service
public class DomoticzService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomoticzService.class);

    @Value("${domoticz.api.url}")
    private String domoticzApiUrl;

    public List<DomoticzResult> getTemperatures() {
        LOGGER.info("Calling Domoticz API");
        RestTemplate restTemplate = new RestTemplate();
        // TODO parametrer URL complete
        Map<String, Object> parametres = new HashMap<>();
        parametres.put("type", "devices");
        parametres.put("filter", "temp");
        parametres.put("used", true);
        parametres.put("order", "Name");
        DomoticzResponse response = restTemplate.getForObject(domoticzApiUrl + "?type={type}&filter={filter}&used={used}&order={order}", DomoticzResponse.class, parametres);

        // TODO ENUM Status
        if (response != null && response.getStatus().equals("OK") && response.getResult() != null) {
            return Arrays.asList(response.getResult());
        } else {
            LOGGER.error("Domoticz result is empty");
            return Collections.emptyList();
        }
    }

}
