package com.cvereterra.cardnetworkservice.api;


import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("card")
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
}
