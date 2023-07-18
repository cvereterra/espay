package com.cvereterra.cardnetworkservice.query;

import com.cvereterra.espaycore.domain.cardnetwork.CardNetworkType;
import com.cvereterra.espaycore.events.cardnetwork.PaymentSessionAuthorizedEvent;
import com.cvereterra.espaycore.queries.cardnetwork.GetCardNetworkTypeByCardNumberQuery;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component

public class CardNetworkProjector {
    private final CardNetworkRepository cardNetworkRepository;
    private Logger logger = LoggerFactory.getLogger(CardNetworkProjector.class);

    public CardNetworkProjector(CardNetworkRepository cardNetworkRepository) {
        this.cardNetworkRepository = cardNetworkRepository;
    }

    @EventHandler
    public void on(PaymentSessionAuthorizedEvent event) {


    }

    @QueryHandler
    public CardNetworkType handler(GetCardNetworkTypeByCardNumberQuery query) {
        // We will simulate that we calculate the CardNetworkType by looking at the first char
        CardNetworkType cardNetwork = CardNetworkType.VISA;
        int number = Character.getNumericValue(query.getCardNumber().charAt(0));
        if (number < 3) cardNetwork = CardNetworkType.VISA;
        else if (number < 6) cardNetwork = CardNetworkType.MASTERCARD;
        else cardNetwork = CardNetworkType.AMEX;
        return cardNetwork;
    }

    private CardNetworkView getExistingPaymentSession(CardNetworkType networkId) {
        Optional<CardNetworkView> cardNetwork = cardNetworkRepository.findById(networkId);
        if (cardNetwork.isEmpty()) {
            throw new IllegalStateException("Card network does not exist");
        }
        return cardNetwork.get();
    }
}
