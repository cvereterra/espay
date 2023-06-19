package com.cvereterra.espaycore.queries.payments;

import lombok.Value;

import java.util.UUID;

@Value
public class FindPaymentSessionByIdQuery {
    UUID sessionId;
}
