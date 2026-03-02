package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.acls.hl7;

import ca.uhn.hl7v2.model.v25.message.ORM_O01;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import ca.uhn.hl7v2.model.v25.segment.PID;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.application.commands.ordonnances.CreateOrdonnanceCmd;
import fr.cnamts.cpam33.ordonnance.domain.kernel.identifiants.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Rpps;
import org.apache.camel.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Hl7InboundTranslator {

    public Object traduire(Message hl7) {
        if ( hl7 instanceof ORM_O01 orm ) {
            return traduireOrm(orm);
        }
        throw new IllegalArgumentException("Message HL7 non supporté");
    }

    private CreateOrdonnanceCmd traduireOrm(ORM_O01 orm) {
        PID pid = orm.getPATIENT().getPID();
        ORC orc = orm.getORDER().getORC();

        return new CreateOrdonnanceCmd(
                new PatientId(pid.getPatientIdentifierList(0).getIDNumber().getValue()),
                new MedecinId("", new Rpps(orc.getOrderingProvider(0).getIDNumber().getValue())),
                List.of() // RXE plus tard
        );
    }

}
