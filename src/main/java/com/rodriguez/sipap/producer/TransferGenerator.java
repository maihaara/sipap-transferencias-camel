package com.rodriguez.sipap.producer;

import org.apache.camel.ProducerTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
public class TransferGenerator {

    private final ProducerTemplate producerTemplate;
    private final Random random = new Random();
    private final String[] bancos = {"ITAU", "ATLAS", "FAMILIAR"};

    public TransferGenerator(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @Scheduled(fixedRate = 5000)
    public void generateTransfer() {
        String bancoOrigen, bancoDestino;
        do {
            bancoOrigen = bancos[random.nextInt(bancos.length)];
            bancoDestino = bancos[random.nextInt(bancos.length)];
        } while (bancoOrigen.equals(bancoDestino));

        int cuenta = 100000 + random.nextInt(900000);
        int monto = 1000 + random.nextInt(4001);

        // Se agrega un ID único para que los consumidores puedan aplicar Idempotent Receiver
        String id = UUID.randomUUID().toString();

        String json = String.format(
            "{\"id\": \"%s\", \"cuenta\": \"%d\", \"monto\": %d, \"banco_origen\": \"%s\", \"banco_destino\": \"%s\"}",
            id, cuenta, monto, bancoOrigen, bancoDestino
        );

        String queue = "rodriguez-" + bancoDestino + "-IN";
        producerTemplate.sendBody("activemq:queue:" + queue, json);
    }
}

