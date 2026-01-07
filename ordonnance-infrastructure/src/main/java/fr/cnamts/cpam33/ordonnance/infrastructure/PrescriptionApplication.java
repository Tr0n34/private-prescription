package fr.cnamts.cpam33.ordonnance.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "fr.cnamts.cpam33.ordonnance")
public class PrescriptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrescriptionApplication.class, args);
    }

}
