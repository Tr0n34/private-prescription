package fr.cnamts.cpam33.ordonnance.infrastructure.akernel;

import fr.cnamts.cpam33.ordonnance.infrastructure.contexts.TestJpaConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add a Configuration for JPA to avoid loading ApplicationContext on each IT<br/>
 * Use this annotation on all JPA Persistence IT<br/>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@Import(TestJpaConfiguration.class)
@ActiveProfiles("integration")
public @interface PersistenceIT {

}
