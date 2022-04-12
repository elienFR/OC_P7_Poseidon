package com.openclassrooms.poseidon.controllers.rest;

import com.openclassrooms.poseidon.domain.Rating;
import com.openclassrooms.poseidon.services.RatingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class is the REST Controller to communicate with database through JSON files
 * with rating objects.
 */
@RestController
@RequestMapping("${api.ver}" + "/rating")
public class RatingRestController {

  private static final Logger LOGGER = LogManager.getLogger(RatingRestController.class);

  @Autowired
  private RatingService ratingService;

  /**
   * this method is one of the Exception Handler to display specific exceptions from
   * this controller.
   *
   * @param ex is the exception handled here
   * @return a map with all errors stacked in this exception.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(
    MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(
      (error) -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
      }
    );
    return errors;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public String handleValidationExceptions(
    IllegalArgumentException ex) {
    return ex.getLocalizedMessage();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public String handleValidationExceptions(
    HttpMessageNotReadableException ex) {
    return "The json you provided cannot be parsed. Some variable does not match the scheme.";
  }

  /**
   * this method is one of the Exception Handler to display specific exceptions from
   * this controller.
   *
   * @param ex is the exception handled here
   * @return a string with the error contained in this exception.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NullPointerException.class)
  public String handleValidationExceptions(
    NullPointerException ex) {
    return ex.getLocalizedMessage();
  }

  /**
   * This method is used to display all ratings in a list.
   *
   * @return the iterable of all ratings contained in database.
   */
  @GetMapping("/list")
  public Iterable<Rating> getAll() {
    LOGGER.info("API Request -> get all ratings...");
    return ratingService.getAll();
  }

  /**
   * This method is used to add a new curve point into database.
   *
   * @param rating is the Json body of the rating you want to add.
   * @return the confirmation message that you correctly added the rating.
   */
  @PostMapping("/add")
  @Transactional
  public ResponseEntity<String> create(@Valid @RequestBody Rating rating) {
    LOGGER.info("API Request -> saving rating : ");
    LOGGER.info(rating.toString());
    Rating savedRating = ratingService.save(rating);
    LOGGER.info("Rating saved successfully");
    return ResponseEntity.ok("Successfully created with id : " + savedRating.getId() + ".");
  }

  /**
   * This method is used to recover a specific rating thanks to its id.
   *
   * @param id is the rating's id you are looking for.
   * @return an optional rating object
   */
  @GetMapping("/list/{id}")
  public Optional<Rating> getById(@PathVariable Integer id) {
    LOGGER.info("API Request -> get curve point with Id : " + id + "...");
    return ratingService.findById(id);
  }

  /**
   * This method is used to update an existing rating from database.
   *
   * @param rating is the curve point you want to modify.
   * @return the confirmation message that you correctly updated the rating.
   */
  @PutMapping("/update")
  @Transactional
  public ResponseEntity<String> update(@Valid @RequestBody Rating rating) {
    LOGGER.info("API Request -> updating rating : ");
    LOGGER.info(rating.toString());
    Rating ratingFromDB = ratingService.findById(rating.getId()).orElseThrow(
      () -> new NullPointerException("No rating found with this id (" + rating.getId() + ")")
    );
    ratingService.update(rating, ratingFromDB);
    LOGGER.info("Rating updated successfully !");
    return ResponseEntity.ok("Rating successfully updated !");
  }

  /**
   * This method is used to delete a curve point from database.
   *
   * @param id is the id of the curve point you want to delete.
   * @return the confirmation message of deletion.
   */
  @DeleteMapping("/delete")
  @Transactional
  public ResponseEntity<String> delete(@RequestParam Integer id) {
    LOGGER.info("API Request -> deleting rating with id : " + id);
    Rating ratingToDelete = ratingService.findById(id)
      .orElseThrow(() -> new NullPointerException("No curve point with id " + id + " exists in DB."));
    ratingService.delete(ratingToDelete);
    return ResponseEntity.ok("Rating with id " + id + " has been deleted successfully.");
  }
}
