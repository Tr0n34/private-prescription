package fr.cnamts.cpam33.ordonnance.infrastructure.in.acls.hl7;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.acls.hl7.AclOutboundProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class Hl7InboundRoutes extends RouteBuilder {

    private final AclOutboundProcessor aclOutboundProcessor;

    public Hl7InboundRoutes(AclOutboundProcessor aclOutboundProcessor) {
        this.aclOutboundProcessor = aclOutboundProcessor;
    }

    @Override
    public void configure() {
        from("mllp://0.0.0.0:2575")
                .routeId("hl7-inbound")
                .unmarshal().hl7()
                .process(aclOutboundProcessor)
                .to("direct:application-entrypoint");
    }
}
