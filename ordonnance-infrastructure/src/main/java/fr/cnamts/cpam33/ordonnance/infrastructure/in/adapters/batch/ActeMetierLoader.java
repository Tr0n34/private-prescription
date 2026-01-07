package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ActeMetierLoader {

    private static final Logger logger = LoggerFactory.getLogger(ActeMetierLoader.class);

    @Value("${ordonnance.loaders.actesMetiers.file:classpath:actes_metiers.json}")
    private String filePath;



}
