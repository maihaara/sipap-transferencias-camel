package com.rodriguez.sipap;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TransferenciaRoute extends RouteBuilder {

    private final String[] bancos = {"ITAU", "ATLAS", "FAMILIAR"};
    private final Random random = new Random();

    @Override
    public void configure() {

        //Timer que genera una transferencia cada 5 segundos
        from("timer:generador?period=5000")
            .process(exchange -> {
                String cuenta = String.valueOf(1000 + random.nextInt(4000));
                int monto = 1000 + random.nextInt(4000);
                String bancoOrigen = bancos[random.nextInt(bancos.length)];
                String bancoDestino;
                do {
                    bancoDestino = bancos[random.nextInt(bancos.length)];
                } while (bancoDestino.equals(bancoOrigen)); // evitar mismo banco

                Transferencia t = new Transferencia(cuenta, monto, bancoOrigen, bancoDestino);
                exchange.getIn().setBody(t.toString()); // JSON como String

                // Ruta dinámica de la cola
                exchange.getIn().setHeader("bancoDestino", bancoDestino);
            })
            .toD("activemq:queue:rodriguez-${header.bancoDestino}-IN");

        // Consumidor ITAU
        from("activemq:queue:rodriguez-ITAU-IN")
            .log("🟦 ITAU recibió: ${body}");

        // Consumidor ATLAS
        from("activemq:queue:rodriguez-ATLAS-IN")
            .log("🟥 ATLAS recibió: ${body}");

        // Puedes agregar otro consumidor si querés FAMILIAR
         from("activemq:queue:rodriguez-FAMILIAR-IN")
            .log("🟩 FAMILIAR recibió: ${body}");
    }
}
