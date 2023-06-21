package com.cvereterra.customersservice.query;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
public class CustomerView {
    @Id
    private UUID customerId;
    private String name;
    private String email;
    private String address;
    private String cardNumber;

    public CustomerView() {
    }
}

