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

@Service
@Transactional
public class BidListService {

  private final static Logger LOGGER = LogManager.getLogger(BidListService.class);
  @Autowired
  private BidListRepository bidListRepository;

  public Iterable<BidList> getAll() {
    LOGGER.info("Contacting DB to get all bids...");
    return bidListRepository.findAll();
  }

  public void delete(BidList bidList) {
    LOGGER.info("Contacting DB to delete bid : " + bidList.toString());
    bidListRepository.delete(bidList);
  }

  public Optional<BidList> findById(Integer id) {
    LOGGER.info("Contacting DB to find bid with id : " + id);
    return bidListRepository.findById(id);
  }

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

  public BidList update(BidList modifiedBidList, BidList bidListToUpdate) {
    LOGGER.info("Contacting DB to update curve point...");
    if (modifiedBidList.getBidListId() != bidListToUpdate.getBidListId()) {
      LOGGER.warn("Your two bids have different id. Update is not possible !");
      throw new RuntimeException("Bid's ID mismatch.");
    }
    //we only set parameters accessible from html form
    bidListToUpdate.setAccount(modifiedBidList.getAccount());
    bidListToUpdate.setType(modifiedBidList.getType());
    bidListToUpdate.setBidQuantity(modifiedBidList.getBidQuantity());
    return bidListRepository.save(bidListToUpdate);
  }
}
