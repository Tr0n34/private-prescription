package fr.cnamts.cpam33.ordonnance.domain.abstracts.traces;

import java.lang.annotation.*;

@Target({ElementType.RECORD_COMPONENT, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface TraceIgnore {

    String reason() default "";

}
