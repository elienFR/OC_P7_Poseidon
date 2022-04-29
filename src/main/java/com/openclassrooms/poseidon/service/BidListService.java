package com.openclassrooms.poseidon.service;

import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.repository.BidListRepository;
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
 * This class is a CRUD service for BidList objects
 *
 */
@Service
@Transactional
public class BidListService {

  private final static Logger LOGGER = LogManager.getLogger(BidListService.class);
  @Autowired
  private BidListRepository bidListRepository;

  /**
   * This method is used to display all bids in iterables.
   *
   * @return is an iterable containing all BidList Object.
   */
  public Iterable<BidList> getAll() {
    LOGGER.info("Contacting DB to get all bids...");
    return bidListRepository.findAll();
  }

  /**
   * This method is used to delete an existing bid from database.
   *
   * @param bidList is the deleted bidList.
   * @Return
   */
  public void delete(BidList bidList) {
    LOGGER.info("Contacting DB to delete bid : " + bidList.toString());
    bidListRepository.delete(bidList);
  }

  /**
   * This method is used to find a specific BidList object from DB thanks to its id.
   *
   * @param id is the id of the bidList you want to retrieve form DB.
   * @return an optional bidList Object.
   */
  public Optional<BidList> findById(Integer id) {
    LOGGER.info("Contacting DB to find bid with id : " + id);
    return bidListRepository.findById(id);
  }

  /**
   * This method is used to save a NEW BidList in the database, and it also adds the creation
   * date to the object.
   * It checks that the object you want to save DOES have a null id.
   * This null property means the object does not exist in DB.
   *
   * @param bid is the BidList object you want to save.
   * @return the saved BidList
   */
  public BidList save(BidList bid) {
    LOGGER.info("Contacting DB to save bid...");
    if (bid.getBidListId() != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        "Forbidden to save a bid with specific id",
        new IllegalArgumentException("Forbidden to save a bid with specific id"));
    }
    //In case the saving is a new bid, then a date is added to its CreationDate attribute
    if (bid.getBidListId() == null) {
      LOGGER.info("Adding CreationDate attributes bid Object...");
      bid.setCreationDate(
        Timestamp.valueOf(LocalDateTime.now())
      );
    }
    return bidListRepository.save(bid);
  }

  /**
   * This method is used to update an EXISTING bidList object.
   *
   * @param modifiedBidList is the object that is going to overwrite the one in DB.
   * @param bidListToUpdate is the object from db to be updated. (the repo that connect to db will basically just check this object id).
   * @return the updated bidList object
   */
  public BidList update(BidList modifiedBidList, BidList bidListToUpdate) {
    LOGGER.info("Contacting DB to update bid...");
    if (modifiedBidList.getBidListId() != bidListToUpdate.getBidListId()) {
      LOGGER.warn("Your two bids have different id. Update is not possible !");
      throw new RuntimeException("Bid's ID mismatch.");
    } else if (existsById(bidListToUpdate.getBidListId())){
      //we only set parameters accessible from html form
      bidListToUpdate.setAccount(modifiedBidList.getAccount());
      bidListToUpdate.setType(modifiedBidList.getType());
      bidListToUpdate.setBidQuantity(modifiedBidList.getBidQuantity());
      return bidListRepository.save(bidListToUpdate);
    } else {
      return null;
    }
  }

  /**
   * This method is used to check the existence of a bidList object in DB thanks to its id.
   *
   * @param bidListId is an integer that represent the bidList id of the bidList object you want to
   *                  check the existence.
   * @return a boolean, true if it exists.
   */
  private boolean existsById(Integer bidListId) {
    return bidListRepository.existsById(bidListId);
  }
}
