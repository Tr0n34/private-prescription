package fr.cnamts.cpam33.ordonnance.infrastructure.akernel;

import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.GlobalControllerAdvice;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.OrdonnanceController;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.PatientController;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add a Controller in Import to avoid loading ApplicationContext on each IT<br/>
 * Use this annotation on all WebMvcTest<br/>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@WebMvcTest
@Import({
        GlobalControllerAdvice.class,
        PatientController.class,
        OrdonnanceController.class
})
@ActiveProfiles("integration")
public @interface WebIT {
}
