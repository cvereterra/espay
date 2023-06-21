package com.cvereterra.cardnetworkservice.query;

import com.cvereterra.espaycore.domain.cardnetwork.CardNetworkType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CardNetworkView {
    @Id
    private CardNetworkType networkId;
    private BigInteger nPaymentsProcessed;
    @ElementCollection
    @CollectionTable(name = "network_daily_accepted_payments")
    @MapKeyColumn(name = "date")
    @Column(name = "daily_payments")
    private Map<Date, BigInteger> dailyProcessedPayments;
}
