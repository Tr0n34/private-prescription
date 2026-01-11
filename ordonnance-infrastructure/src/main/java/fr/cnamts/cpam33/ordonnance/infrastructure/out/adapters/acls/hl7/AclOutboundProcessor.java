package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.acls.hl7;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.ExportMedecinDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.ExportPatientDto;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class AclOutboundProcessor implements Processor {

    private final HL7PatientMedecinMapper mapper;

    public AclOutboundProcessor(HL7PatientMedecinMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        ExportPatientDto patient = exchange.getIn().getHeader("patient", ExportPatientDto.class);
        ExportMedecinDto medecin = exchange.getIn().getHeader("medecin", ExportMedecinDto.class);

        exchange.getIn().setBody(
                mapper.buildMessage(patient, medecin)
        );
    }
}
