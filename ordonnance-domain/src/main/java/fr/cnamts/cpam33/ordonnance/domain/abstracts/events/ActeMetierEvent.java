package fr.cnamts.cpam33.ordonnance.domain.abstracts.events;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.enums.ActeMetierCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActeMetierEvent {

    ActeMetierCode value();

}
