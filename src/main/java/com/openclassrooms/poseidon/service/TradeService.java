package com.openclassrooms.poseidon.service;

import com.openclassrooms.poseidon.domain.Trade;
import com.openclassrooms.poseidon.repository.TradeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class TradeService {

  private static final Logger LOGGER = LogManager.getLogger(TradeService.class);

  @Autowired
  private TradeRepository tradeRepository;

  public Iterable<Trade> getAll() {
    LOGGER.info("Contacting DB to get all trades...");
    return tradeRepository.findAll();
  }

  public Trade save(Trade trade) {
    LOGGER.info("Contacting DB to save trade...");
    //In case the saving is a new trade, then a date is added to its CreationDate attribute
    if (trade.getTradeId() != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        "Forbidden to save a curve point with specific id",
        new IllegalArgumentException("Forbidden to save a curve point with specific id"));
    }
    if (trade.getTradeId() == null) {
      LOGGER.info("Adding CreationDate attributes curvePoint Object...");
      trade.setCreationDate(
        Timestamp.valueOf(LocalDateTime.now())
      );
    }
    return tradeRepository.save(trade);
  }

  public Optional<Trade> findById(Integer id) {
    LOGGER.info("Contacting DB to find trade with id : " + id);
    return tradeRepository.findById(id);
  }

  public Trade update(Trade modifiedTrade, Trade tradeToUpdate) {
    LOGGER.info("Contacting DB to update trade...");
    if (modifiedTrade.getTradeId() != tradeToUpdate.getTradeId()) {
      LOGGER.warn("Your two trades have different id. Update is not possible !");
      throw new RuntimeException("Trades ID mismatch.");
    }
    //we only set parameters accessible from html form
    tradeToUpdate.setAccount(modifiedTrade.getAccount());
    tradeToUpdate.setType(modifiedTrade.getType());
    tradeToUpdate.setBuyQuantity(modifiedTrade.getBuyQuantity());

    return tradeRepository.save(tradeToUpdate);
  }

  public void delete(Trade tradeToDelete) {
    LOGGER.info("Contacting DB to delete trade : " + tradeToDelete.toString());
    tradeRepository.delete(tradeToDelete);
  }
}
