package com.rodriguez.sipap.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ValidadorProcessor implements Processor {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> datos = mapper.readValue(body, Map.class);

        String fecha = (String) datos.get("fecha");
        Integer id = (Integer) datos.get("id_transaccion");

        String hoy = LocalDate.now().format(formatter);

        if (fecha.equals(hoy)) {
            exchange.getIn().setBody("{\"id_transaccion\":" + id + ", \"mensaje\":\"Transferencia procesada exitosamente\"}");
        } else {
            exchange.getIn().setBody("{\"id_transaccion\":" + id + ", \"mensaje\":\"Mensaje caducado\"}");
        }
    }
}

