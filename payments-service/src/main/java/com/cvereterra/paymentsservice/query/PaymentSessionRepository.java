package com.cvereterra.paymentsservice.query;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentSessionRepository extends JpaRepository<PaymentSessionView, UUID> {
}
