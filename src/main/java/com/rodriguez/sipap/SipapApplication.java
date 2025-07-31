package com.rodriguez.sipap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SipapApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(SipapApplication.class, args);

        // Mantener la app viva
        Thread.currentThread().join();
    }
}
