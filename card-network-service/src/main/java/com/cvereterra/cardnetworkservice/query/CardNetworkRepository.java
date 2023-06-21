package com.cvereterra.cardnetworkservice.query;

import com.cvereterra.espaycore.domain.cardnetwork.CardNetworkType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardNetworkRepository extends JpaRepository<CardNetworkView, CardNetworkType> {
}
