package fr.gaeljw.domoticz2mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Domoticz2mongoApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Domoticz2mongoApplication.class);
        app.setWebEnvironment(false);
        app.run(args);
	}
}
