package com.cvereterra.espaycore.queries.customer;

import lombok.Value;

import java.util.UUID;

@Value
public class FindCustomerByIdQuery {
    UUID customerId;
}
