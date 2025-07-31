package com.rodriguez.sipap.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TransferRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {

        from("activemq:queue:rodriguez-ITAU-IN")
            .log("Banco ITAU recibió: ${body}");

        from("activemq:queue:rodriguez-ATLAS-IN")
            .log("Banco ATLAS recibió: ${body}");
    }
}
