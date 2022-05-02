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

/**
 *
 * This class is a CRUD service for trade objects
 *
 */
@Service
@Transactional
public class TradeService {

  private static final Logger LOGGER = LogManager.getLogger(TradeService.class);

  @Autowired
  private TradeRepository tradeRepository;

  /**
   * This method is used to display all trade in iterables.
   *
   * @return is an iterable containing all trade Object.
   */
  public Iterable<Trade> getAll() {
    LOGGER.info("Contacting DB to get all trades...");
    return tradeRepository.findAll();
  }

  /**
   * This method is used to save a NEW trade in the database, and it also adds the creation
   * date to the object.
   * It checks that the object you want to save DOES have a null id.
   * This null property means the object does not exist in DB.
   *
   * @param trade is the trade object you want to save.
   * @return the saved trade
   */
  public Trade save(Trade trade) {
    LOGGER.info("Contacting DB to save trade...");
    //In case the saving is a new trade, then a date is added to its CreationDate attribute
    if (trade.getTradeId() != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        "Forbidden to save a trade with specific id",
        new IllegalArgumentException("Forbidden to save a trade with specific id"));
    }
    LOGGER.info("Adding CreationDate attributes trade Object...");
    trade.setCreationDate(
      Timestamp.valueOf(LocalDateTime.now())
    );
    return tradeRepository.save(trade);
  }

  /**
   * This method is used to find a specific trade object from DB thanks to its id.
   *
   * @param id is the id of the trade you want to retrieve form DB.
   * @return an optional trade Object.
   */
  public Optional<Trade> findById(Integer id) {
    LOGGER.info("Contacting DB to find trade with id : " + id);
    return tradeRepository.findById(id);
  }

  /**
   * This method is used to update an EXISTING trade object.
   *
   * @param modifiedTrade is the object that is going to overwrite the one in DB.
   * @param tradeToUpdate is the object from db to be updated. (the repo that connect to db will basically just check this object id).
   * @return the updated trade object
   */
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

  /**
   * This method is used to delete an existing trade from database.
   *
   * @param tradeToDelete is the deleted trade.
   */
  public void delete(Trade tradeToDelete) {
    LOGGER.info("Contacting DB to delete trade : " + tradeToDelete.toString());
    tradeRepository.delete(tradeToDelete);
  }
}
