package com.openclassrooms.poseidon.repositories;

import com.openclassrooms.poseidon.domain.Trade;
import org.springframework.data.repository.CrudRepository;


public interface TradeRepository extends CrudRepository<Trade, Integer> {
}
