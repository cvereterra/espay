package com.cvereterra.paymentsservice.query;

import com.cvereterra.paymentsservice.command.PaymentSessionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@Setter
public class PaymentSessionView {
    @Id
    private UUID sessionId;
    private UUID merchantId;
    private UUID customerId;
    private String currency;
    private PaymentSessionStatus status;
    private BigDecimal amount;

    public PaymentSessionView() {
    }
}
