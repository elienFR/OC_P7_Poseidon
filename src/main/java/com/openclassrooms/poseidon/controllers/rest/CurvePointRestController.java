package com.openclassrooms.poseidon.controllers.rest;

import com.nimbusds.jose.jwk.Curve;
import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.domain.CurvePoint;
import com.openclassrooms.poseidon.services.CurvePointService;
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
 * with curve point objects.
 */
@RestController
@RequestMapping("${api.ver}" + "/curvePoint")
public class CurvePointRestController {

  private static final Logger LOGGER = LogManager.getLogger(CurvePointRestController.class);

  @Autowired
  private CurvePointService curvePointService;

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
   * This method is used to display all curve point in a list.
   *
   * @return the iterable of all curve points contained in database.
   */
  @GetMapping("/list")
  public Iterable<CurvePoint> getAll() {
    LOGGER.info("API Request -> get all curve points...");
    return curvePointService.getAll();
  }

  /**
   * This method is used to add a new curve point into database.
   *
   * @param curvePoint is the Json body of the curve point you want to add.
   * @return the confirmation message that you correctly added the bid.
   */
  @PostMapping("/add")
  @Transactional
  public ResponseEntity<String> create(@Valid @RequestBody CurvePoint curvePoint) {
    LOGGER.info("API Request -> saving curve point : ");
    LOGGER.info(curvePoint.toString());
    CurvePoint savedCurvePoint = curvePointService.save(curvePoint);
    LOGGER.info("bid saved successfully");
    return ResponseEntity.ok("Successfully created with id : " + savedCurvePoint.getId() + ".");
  }

  /**
   * This method is used to recover a specific curve point thanks to its id.
   *
   * @param id is the curve point's id you are looking for.
   * @return an optional bidList object
   */
  @GetMapping("/list/{id}")
  public Optional<CurvePoint> getById(@PathVariable Integer id) {
    LOGGER.info("API Request -> get curve point with Id : " + id + "...");
    return curvePointService.findById(id);
  }

  /**
   * This method is used to update an existing curve point from database.
   *
   * @param curvePoint is the curve point you want to modify.
   * @return the confirmation message that you correctly updated the bid.
   */
  @PutMapping("/update")
  @Transactional
  public ResponseEntity<String> update(CurvePoint curvePoint) {
    LOGGER.info("API Request -> updating curve point : ");
    LOGGER.info(curvePoint.toString());
    curvePointService.findById(curvePoint.getId()).orElseThrow(
      () -> new NullPointerException("No curve point found with this id (" + curvePoint.getId() + ")")
    );
    LOGGER.info("bid updated successfully");
    curvePointService.save(curvePoint);
    return ResponseEntity.ok("Curve point successfully updated !");
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
    LOGGER.info("API Request -> deleting curve point with id : " + id);
    CurvePoint curvePointToDelete = curvePointService.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Invalid curve point id:" + id));
    curvePointService.delete(curvePointToDelete);
    return ResponseEntity.ok("Curve point with id " + id + " has been deleted successfully.");
  }
}
