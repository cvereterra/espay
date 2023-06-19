package com.cvereterra.espaycore.queries.payments;

import lombok.Data;

import java.util.UUID;

@Data
public class FindPaymentSessionByIdQuery {
    private final UUID sessionId;
}
