package com.openclassrooms.poseidon.controllers.rest;

import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.services.BidListService;
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

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

/**
 * This class is the REST Controller to communicate with database through JSON files.
 */
@RestController
@RequestMapping("${api.ver}" + "/bidList")
public class BidListRestController {

  private static final Logger LOGGER = LogManager.getLogger(BidListRestController.class);
  @Autowired
  private BidListService bidListService;

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
   * This method is used to display all bids in a list.
   *
   * @return the iterable of all bids contained in database.
   */
  @GetMapping("/list")
  public Iterable<BidList> getAll() {
    LOGGER.info("API Request -> get all the bids...");
    return bidListService.getAll();
  }

  /**
   * This method is used to recover a specific bid thanks to its id.
   *
   * @param id is the bid's id you are looking for.
   * @return an optional bidList object
   */
  @GetMapping("/list/{id}")
  public Optional<BidList> getById(@PathVariable Integer id) {
    LOGGER.info("API Request -> get bid with Id : " + id + "...");
    return bidListService.findById(id);
  }

  /**
   * This method is used to delete a bid from database.
   *
   * @param id is the id of the bid you want to delete.
   * @return the confirmation message of deletion.
   */
  @DeleteMapping("/delete")
  @Transactional
  public ResponseEntity<String> delete(@RequestParam Integer id) {
    LOGGER.info("API Request -> deleting bid with id : " + id);
    BidList bidListToDelete = bidListService.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Invalid bidList Id:" + id));
    bidListService.delete(bidListToDelete);
    return ResponseEntity.ok("BidList with id " + id + " has been deleted successfully.");
  }

  /**
   * This method is used to add a new a bid into database.
   *
   * @param bid is the Json body of the bid you want to add.
   * @return the confirmation message that you correctly added the bid.
   */
  @PostMapping("/add")
  @Transactional
  public ResponseEntity<String> create(@Valid @RequestBody BidList bid) {
    LOGGER.info("API Request -> saving bid : ");
    LOGGER.info(bid.toString());
    LOGGER.info("bid saved successfully");
    BidList savedBid = bidListService.save(bid);
    return ResponseEntity.ok("Successfully created with id : " + savedBid.getBidListId() + ".");
  }

  /**
   * This method is used to update an existing bid from database.
   *
   * @param bid is the bid you want to modify.
   * @return the confirmation message that you correctly updated the bid.
   */
  @PutMapping("/update")
  @Transactional
  public ResponseEntity<String> update(@Valid @RequestBody BidList bid) {
    LOGGER.info("API Request -> updating bid : ");
    LOGGER.info(bid.toString());
    bidListService.findById(bid.getBidListId()).orElseThrow(
      () -> new NullPointerException("No bid found with this id (" + bid.getBidListId() + ")")
    );
    LOGGER.info("bid updated successfully");
    bidListService.save(bid);
    return ResponseEntity.ok("Bid successfully updated");
  }
}
