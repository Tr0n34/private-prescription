package fr.cnamts.cpam33.ordonnance.infrastructure.in.acls.hl7;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class AclInboundProcessor implements Processor {

    private final Hl7InboundTranslator translator;

    public AclInboundProcessor(Hl7InboundTranslator translator) {
        this.translator = translator;
    }

    @Override
    public void process(Exchange exchange) {
        Message hl7Message = exchange.getIn().getBody(Message.class);

        // Traduction HL7 → Commande interne
        Object commande = translator.traduire(hl7Message);

        exchange.getIn().setBody(commande);
    }
}
