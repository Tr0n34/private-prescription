package fr.cnamts.cpam33.ordonnance.domain.kernel.traces;

import java.lang.annotation.*;

@Target({ElementType.RECORD_COMPONENT, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TraceMask {

    TraceMaskMode mode() default TraceMaskMode.FULL;

    int keep() default 0;

    String replacement() default "***";

}
