package com.openclassrooms.poseidon.controllers.rest;

import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.services.BidListService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("${api.ver}" +"/bidList")
public class BidListRestController {

  private static final Logger LOGGER = LogManager.getLogger(BidListRestController.class);
  @Autowired
  private BidListService bidListService;

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

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NullPointerException.class)
  public String handleValidationExceptions(
    NullPointerException ex) {
    return ex.getLocalizedMessage();
  }

  @GetMapping("/list")
  public Iterable<BidList> getAll() {
    LOGGER.info("API Request -> get all the bids...");
    return bidListService.getAll();
  }

  @DeleteMapping("/delete")
  @Transactional
  public ResponseEntity<String> delete(@RequestParam Integer id) {
    LOGGER.info("API Request -> deleting bid with id : " + id);
    BidList bidListToDelete = bidListService.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Invalid bidList Id:" + id));
    bidListService.delete(bidListToDelete);
    return ResponseEntity.ok("BidList with id " + id + " has been deleted successfully.");
  }

  @PostMapping("/add/validate")
  @Transactional
  public ResponseEntity<String> create(@Valid @RequestBody BidList bid) {
    LOGGER.info("API Request -> saving bid : ");
    LOGGER.info(bid.toString());
    LOGGER.info("bid saved successfully");
    bidListService.save(bid);
    return ResponseEntity.ok("Successfully posted");
  }

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
