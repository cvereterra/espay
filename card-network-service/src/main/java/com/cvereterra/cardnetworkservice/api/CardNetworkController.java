package com.cvereterra.cardnetworkservice.api;


import com.cvereterra.espaycore.domain.cardnetwork.CardNetworkType;
import com.cvereterra.espaycore.queries.cardnetwork.GetCardNetworkTypeByCardNumberQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("card-networks")
public class CardNetworkController {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @Autowired
    public CardNetworkController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @GetMapping("/health")
    public Map<String, String> getHealth() {
        return Map.of("status", "Ok");
    }

    @GetMapping("/get-card-network")
    public CompletableFuture<Map<String, String>> getCardNetworkFromCardNumber(@RequestParam(name = "cardNumber") String cardNumber) {
        CompletableFuture<CardNetworkType> cardType = queryGateway.query(new GetCardNetworkTypeByCardNumberQuery(cardNumber), ResponseTypes.instanceOf(CardNetworkType.class));
        return cardType.thenApplyAsync((type -> Map.of("type", type.name())));
    }
}
