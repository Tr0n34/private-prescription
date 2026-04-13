package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BoundedContextHint {

    String value() default "API_ERROR";

}
