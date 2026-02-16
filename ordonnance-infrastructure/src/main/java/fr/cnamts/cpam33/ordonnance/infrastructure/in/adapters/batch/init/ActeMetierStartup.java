package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.init;

import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.ActeMetierLoader;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

@Component
public class ActeMetierStartup implements SmartInitializingSingleton {

    private final ActeMetierLoader acteMetierLoader;

    public ActeMetierStartup(ActeMetierLoader acteMetierLoader) {
        this.acteMetierLoader = acteMetierLoader;
    }

    @Override
    public void afterSingletonsInstantiated() {
        acteMetierLoader.reload();
    }

}
