package com.cvereterra.espaycore.queries.customer;

import lombok.Data;

import java.util.UUID;

@Data
public class FindCustomerByIdQuery {
    private final UUID customerId;
}
