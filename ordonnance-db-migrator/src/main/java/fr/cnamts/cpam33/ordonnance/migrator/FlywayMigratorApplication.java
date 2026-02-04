package fr.cnamts.cpam33.ordonnance.migrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlywayMigratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlywayMigratorApplication.class, args);
    }

}
