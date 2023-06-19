package com.cvereterra.cardnetworkservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({AxonConfig.class})
public class CardNetworkApplication {
    public static void main(String[] args) {
        SpringApplication.run(CardNetworkApplication.class, args);
    }
}