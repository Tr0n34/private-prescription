package fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.stubs;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.kernel.traces.Traceable;
import org.springframework.stereotype.Service;

@ActeMetierEvent(ActeMetierCode.ORD_CREER)
@Service
public class DummyActeMetierService {

    public String ok(Traceable cmd) {
        return "RESULT_OK";
    }

    public void ko(Traceable cmd) {
        throw new IllegalStateException("BOOM");
    }

    public String withoutTrace() {
        return "NO_TRACE";
    }
}
