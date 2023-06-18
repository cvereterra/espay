package com.cvereterra.customersservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({AxonConfig.class})
public class CustomersApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomersApplication.class, args);
    }
}