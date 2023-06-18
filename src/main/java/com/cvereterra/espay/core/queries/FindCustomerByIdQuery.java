package com.cvereterra.espay.core.queries;

import lombok.Data;

import java.util.UUID;

@Data
public class FindCustomerByIdQuery {
    private final UUID customerId;
}
