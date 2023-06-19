package com.cvereterra.paymentsservice.command;

public enum PaymentSessionStatus {
    CREATED,
    ASSIGNED_TO_CUSTOMER,
    AUTHORIZED_BY_CARD_NETWORK,
    REJECTED_BY_CARD_NETWORK,
    ADQUIRING,
    ADQUIRED,
    TRANSFERED_TO_MERCHANT,
    CANCELED
}
