package com.cvereterra.espaycore.commands.adquirer;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public class RejectPaymentAdquirementCommand {
    @TargetAggregateIdentifier
    UUID sessionId;
}
