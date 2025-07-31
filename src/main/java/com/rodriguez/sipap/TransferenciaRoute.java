package com.rodriguez.sipap;

import com.rodriguez.sipap.processor.ValidadorProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TransferenciaRoute extends RouteBuilder {

    private final String[] bancos = {"ITAU", "ATLAS", "FAMILIAR"};
    private final Random random = new Random();

    @Override
    public void configure() {

        // Generador de transferencias (cada 5 segundos)
        from("timer:generador?period=5000")
            .process(exchange -> {
                String cuenta = String.valueOf(1000 + random.nextInt(4000));
                int monto = 1000 + random.nextInt(4000);
                String bancoOrigen = bancos[random.nextInt(bancos.length)];
                String bancoDestino;
                do {
                    bancoDestino = bancos[random.nextInt(bancos.length)];
                } while (bancoDestino.equals(bancoOrigen));

                Transferencia t = new Transferencia(cuenta, monto, bancoOrigen, bancoDestino);
                exchange.getIn().setBody(t.toString());
                exchange.getIn().setHeader("nombreCola", "rodriguez-" + bancoDestino + "-IN");
                exchange.getIn().setHeader("bancoDestino", bancoDestino);
            })
            .log("Generando transferencia y enviando a cola de validación")
            .to("activemq:queue:rodriguez-VALIDAR-IN");

        // Proceso de validación antes de pasar al banco destino
        from("activemq:queue:rodriguez-VALIDAR-IN")
            .routeId("validacion-transferencia")
            .log("Validando transferencia: ${body}")
            .process(new ValidadorProcessor())
            .toD("activemq:queue:${header.nombreCola}");

        // Consumidor ITAU
        from("activemq:queue:rodriguez-ITAU-IN")
            .log("ITAU recibió: ${body}");

        // Consumidor ATLAS
        from("activemq:queue:rodriguez-ATLAS-IN")
            .log("ATLAS recibió: ${body}");

        // Consumidor FAMILIAR
        from("activemq:queue:rodriguez-FAMILIAR-IN")
            .log("FAMILIAR recibió: ${body}");
    }
}

