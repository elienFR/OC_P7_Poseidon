package com.openclassrooms.poseidon.service;

import com.openclassrooms.poseidon.domain.Rating;
import com.openclassrooms.poseidon.repository.RatingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 *
 * This class is a CRUD service for Ratings objects
 *
 */
@Service
@Transactional
public class RatingService {

  private static final Logger LOGGER = LogManager.getLogger(RatingService.class);

  @Autowired
  private RatingRepository ratingRepository;

  /**
   * This method is used to display all rating in iterables.
   *
   * @return is an iterable containing all rating Object.
   */
  public Iterable<Rating> getAll() {
    LOGGER.info("Contacting DB to get all ratings...");
    return ratingRepository.findAll();
  }

  /**
   * This method is used to save a NEW rating in the database.
   * It checks that the object you want to save DOES have a null id.
   * This null property means the object does not exist in DB.
   *
   * @param rating is the BidList object you want to save.
   * @return the saved rating
   */
  public Rating save(Rating rating) {
    LOGGER.info("Contacting DB to save rating...");
    if (rating.getId() != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        "Forbidden to save a rating with specific id",
        new IllegalArgumentException("Forbidden to save a rating with specific id"));
    }
    return ratingRepository.save(rating);
  }

  /**
   * This method is used to find a specific rating object from DB thanks to its id.
   *
   * @param id is the id of the rating you want to retrieve form DB.
   * @return an optional rating Object.
   */
  public Optional<Rating> findById(Integer id) {
    LOGGER.info("Contacting DB to find rating with id : " + id);
    return ratingRepository.findById(id);
  }

  /**
   * This method is used to update an EXISTING rating object.
   *
   * @param modifiedRating is the object that is going to overwrite the one in DB.
   * @param ratingToUpdate is the object from db to be updated. (the repo that connect to db will basically just check this object id).
   * @return the updated rating object
   */
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

  /**
   * This method is used to delete an existing rating from database.
   *
   * @param ratingToDelete is the deleted rating.
   */
  public void delete(Rating ratingToDelete) {
    LOGGER.info("Contacting DB to delete rating : " + ratingToDelete.toString());
    ratingRepository.delete(ratingToDelete);
  }
}
