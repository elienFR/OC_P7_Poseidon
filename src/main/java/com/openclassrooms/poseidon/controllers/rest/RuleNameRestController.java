package com.openclassrooms.poseidon.controllers.rest;

import com.openclassrooms.poseidon.controllers.RuleNameController;
import com.openclassrooms.poseidon.domain.Rating;
import com.openclassrooms.poseidon.domain.RuleName;
import com.openclassrooms.poseidon.services.RuleNameService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.digester.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
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
 * with rule name objects.
 */
@RestController
@RequestMapping("${api.ver}" + "/ruleName")
public class RuleNameRestController {

  private static final Logger LOGGER = LogManager.getLogger(RuleNameRestController.class);

  @Autowired
  private RuleNameService ruleNameService;

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
   * This method is used to display all rule names in a list.
   *
   * @return the iterable of all rule names contained in database.
   */
  @GetMapping("/list")
  public Iterable<RuleName> getAll() {
    LOGGER.info("API Request -> get all rule names...");
    return ruleNameService.getAll();
  }

  /**
   * This method is used to add a new rule name point into database.
   *
   * @param ruleName is the Json body of the rule name you want to add.
   * @return the confirmation message that you correctly added the bid.
   */
  @PostMapping("/add")
  @Transactional
  public ResponseEntity<String> create(@Valid @RequestBody RuleName ruleName) {
    LOGGER.info("API Request -> saving rule name : ");
    LOGGER.info(ruleName.toString());
    RuleName savedRuleName = ruleNameService.save(ruleName);
    LOGGER.info("Rating saved successfully");
    return ResponseEntity.ok("Successfully created with id : " + savedRuleName.getId() + ".");

  }

  /**
   * This method is used to recover a specific rule name thanks to its id.
   *
   * @param id is the rule name's id you are looking for.
   * @return an optional bidList object
   */
  @GetMapping("/list/{id}")
  public Optional<RuleName> getById(@PathVariable Integer id) {
    LOGGER.info("API Request -> get rule name with Id : " + id + "...");
    return ruleNameService.findById(id);
  }

  /**
   * This method is used to update an existing rule name from database.
   *
   * @param ruleName is the curve point you want to modify.
   * @return the confirmation message that you correctly updated the rule name.
   */
  @PutMapping("/update")
  @Transactional
  public ResponseEntity<String> update(@Valid @RequestBody RuleName ruleName) {
    LOGGER.info("API Request -> updating rule name : ");
    LOGGER.info(ruleName.toString());
    RuleName ruleNameFromDB = ruleNameService.findById(ruleName.getId()).orElseThrow(
      () -> new NullPointerException("No rule name found with this id (" + ruleName.getId() + ")")
    );
    ruleNameService.update(ruleName, ruleNameFromDB);
    LOGGER.info("Rule name updated successfully !");
    return ResponseEntity.ok("Rule name successfully updated !");
  }

  /**
   * This method is used to delete a rule name from database.
   *
   * @param id is the id of the rule name you want to delete.
   * @return the confirmation message of deletion.
   */
  @DeleteMapping("/delete")
  @Transactional
  public ResponseEntity<String> delete(@RequestParam Integer id) {
    LOGGER.info("API Request -> deleting rule name with id : " + id);
    RuleName ruleNameToDelete = ruleNameService.findById(id)
      .orElseThrow(() -> new NullPointerException("No rule name with id " + id + " exists in DB."));
    ruleNameService.delete(ruleNameToDelete);
    return ResponseEntity.ok("Rule name with id " + id + " has been deleted successfully.");

  }
}
