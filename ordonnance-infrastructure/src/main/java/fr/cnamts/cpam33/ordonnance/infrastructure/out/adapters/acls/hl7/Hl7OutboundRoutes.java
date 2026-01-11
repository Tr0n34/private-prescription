package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.acls.hl7;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class Hl7OutboundRoutes extends RouteBuilder {

    private final AclOutboundProcessor aclOutboundProcessor;

    public Hl7OutboundRoutes(AclOutboundProcessor aclOutboundProcessor) {
        this.aclOutboundProcessor = aclOutboundProcessor;
    }

    @Override
    public void configure() {
        from("direct:export-ordonnance")
                .routeId("hl7-out-ordonnance")
                .process(aclOutboundProcessor)
                .marshal().hl7()
                .to("mllp://dest:2575");
    }

}
