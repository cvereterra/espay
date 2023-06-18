package com.cvereterra.espay.query;


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

    public CustomerView() {
    }
}

