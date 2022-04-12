package com.openclassrooms.poseidon.repository;

import com.openclassrooms.poseidon.domain.Trade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends CrudRepository<Trade, Integer> {
}
