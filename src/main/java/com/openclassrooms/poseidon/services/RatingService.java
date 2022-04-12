package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.Rating;
import com.openclassrooms.poseidon.repositories.RatingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class RatingService {

  private static final Logger LOGGER = LogManager.getLogger(RatingService.class);

  @Autowired
  private RatingRepository ratingRepository;


  public Iterable<Rating> getAll() {
    LOGGER.info("Contacting DB to get all ratings...");
    return ratingRepository.findAll();
  }

  public Rating save(Rating rating) {
    LOGGER.info("Contacting DB to save rating...");
    if (rating.getId() != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        "Forbidden to save a rating with specific id",
        new IllegalArgumentException("Forbidden to save a rating with specific id"));
    }
    return ratingRepository.save(rating);
  }

  public Optional<Rating> findById(Integer id) {
    LOGGER.info("Contacting DB to find rating with id : " + id);
    return ratingRepository.findById(id);
  }

  public Rating update(Rating modifiedRating, Rating ratingToUpdate) {
    LOGGER.info("Contacting DB to update rating...");
    if (modifiedRating.getId() != ratingToUpdate.getId()) {
      LOGGER.warn("Your two ratings have different id. Update is not possible !");
      throw new RuntimeException("Rating ID mismatch.");
    }
    //we only set parameters accessible from html form
    ratingToUpdate.setMoodysRating(modifiedRating.getMoodysRating());
    ratingToUpdate.setSandPRating(modifiedRating.getSandPRating());
    ratingToUpdate.setFitchRating(modifiedRating.getFitchRating());
    ratingToUpdate.setOrderNumber(modifiedRating.getOrderNumber());
    return ratingRepository.save(ratingToUpdate);
  }

  public void delete(Rating ratingToDelete) {
    LOGGER.info("Contacting DB to delete rating : " + ratingToDelete.toString());
    ratingRepository.delete(ratingToDelete);
  }
}
