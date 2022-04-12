package com.openclassrooms.poseidon.controller.rest;

import com.openclassrooms.poseidon.domain.Trade;
import com.openclassrooms.poseidon.service.TradeService;
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
 * with trade objects.
 */
@RestController
@RequestMapping("${api.ver}" + "/trade")
public class TradeRestController {

  private static final Logger LOGGER = LogManager.getLogger(TradeRestController.class);

  @Autowired
  private TradeService tradeService;


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
   * This method is used to display all trades in a list.
   *
   * @return the iterable of all trades contained in database.
   */
  @GetMapping("/list")
  public Iterable<Trade> getAll() {
    LOGGER.info("API Request -> get all trades...");
    return tradeService.getAll();
  }

  /**
   * This method is used to add a new trade into database.
   *
   * @param trade is the Json body of the trade you want to add.
   * @return the confirmation message that you correctly added the bid.
   */
  @PostMapping("/add")
  @Transactional
  public ResponseEntity<String> create(@Valid @RequestBody Trade trade) {
    LOGGER.info("API Request -> saving trade : ");
    LOGGER.info(trade.toString());
    Trade savedTrade = tradeService.save(trade);
    LOGGER.info("Trade saved successfully");
    return ResponseEntity.ok("Successfully created with id : " + savedTrade.getTradeId() + ".");
  }

  /**
   * This method is used to recover a specific trade thanks to its id.
   *
   * @param id is the trade's id you are looking for.
   * @return an optional trade object
   */
  @GetMapping("/list/{id}")
  public Optional<Trade> getById(@PathVariable Integer id) {
    LOGGER.info("API Request -> get trade with Id : " + id + "...");
    return tradeService.findById(id);
  }

  /**
   * This method is used to update an existing trade from database.
   *
   * @param trade is the curve point you want to modify.
   * @return the confirmation message that you correctly updated the trade.
   */
  @PutMapping("/update")
  @Transactional
  public ResponseEntity<String> update(@Valid @RequestBody Trade trade) {
    LOGGER.info("API Request -> updating trade : ");
    LOGGER.info(trade.toString());
    Trade tradeFromDB = tradeService.findById(trade.getTradeId()).orElseThrow(
      () -> new NullPointerException("No trade found with this id (" + trade.getTradeId() + ")")
    );
    tradeService.update(trade, tradeFromDB);
    LOGGER.info("Trade updated successfully !");
    return ResponseEntity.ok("Trade successfully updated !");
  }

  /**
   * This method is used to delete a trade from database.
   *
   * @param id is the id of the trade you want to delete.
   * @return the confirmation message of deletion.
   */
  @DeleteMapping("/delete")
  @Transactional
  public ResponseEntity<String> delete(@RequestParam Integer id) {
    LOGGER.info("API Request -> deleting trade with id : " + id);
    Trade tradeToDelete = tradeService.findById(id)
      .orElseThrow(() -> new NullPointerException("No trade with id " + id + " exists in DB."));
    tradeService.delete(tradeToDelete);
    return ResponseEntity.ok("Trade with id " + id + " has been deleted successfully.");
  }
}
