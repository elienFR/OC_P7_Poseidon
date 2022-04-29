package com.openclassrooms.poseidon.service;

import com.openclassrooms.poseidon.domain.CurvePoint;
import com.openclassrooms.poseidon.repository.CurvePointRepository;
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
 * This class is a CRUD service for CurvePoint objects
 *
 */
@Service
@Transactional
public class CurvePointService {

  private static final Logger LOGGER = LogManager.getLogger(CurvePointService.class);

  @Autowired
  private CurvePointRepository curvePointRepository;

  /**
   * This method is used to display all curvePoint in iterables.
   *
   * @return is an iterable containing all CurvePoint Object.
   */
  public Iterable<CurvePoint> getAll() {
    LOGGER.info("Contacting DB to get all curve points...");
    return curvePointRepository.findAll();
  }


  /**
   * This method is used to save a NEW CurvePoint in the database, and it also adds the creation
   * date to the object.
   * It checks that the object you want to save DOES have a null id.
   * This null property means the object does not exist in DB.
   *
   * @param curvePoint is the curvePoint object you want to save.
   * @return the saved curvePoint
   */
  public CurvePoint save(CurvePoint curvePoint) {
    LOGGER.info("Contacting DB to save curve point...");
    if (curvePoint.getId() != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        "Forbidden to save a curve point with specific id",
        new IllegalArgumentException("Forbidden to save a curve point with specific id"));
    }
    //In case the saving is a new curve point, then a date is added to its CreationDate attribute
    if (curvePoint.getId() == null) {
      LOGGER.info("Adding CreationDate attributes curvePoint Object...");
      curvePoint.setCreationDate(
        Timestamp.valueOf(LocalDateTime.now())
      );
    }
    return curvePointRepository.save(curvePoint);
  }

  /**
   * This method is used to find a specific CurvePoint object from DB thanks to its id.
   *
   * @param id is the id of the curvePoint you want to retrieve form DB.
   * @return an optional curvePoint Object.
   */
  public Optional<CurvePoint> findById(Integer id) {
    LOGGER.info("Contacting DB to find curve point with id : " + id);
    return curvePointRepository.findById(id);
  }

  /**
   * This method is used to delete an existing curvePoint from database.
   *
   * @param curvePointToDelete is the deleted curvePoint.
   */
  public void delete(CurvePoint curvePointToDelete) {
    LOGGER.info("Contacting DB to delete curve point : " + curvePointToDelete.toString());
    curvePointRepository.delete(curvePointToDelete);
  }

  /**
   * This method is used to update an EXISTING curvePoint object.
   *
   * @param modifiedCurvePoint is the object that is going to overwrite the one in DB.
   * @param curvePointToUpdate is the object from db to be updated. (the repo that connect to db will basically just check this object id).
   * @return the updated curvePoint object
   */
  public CurvePoint update(CurvePoint modifiedCurvePoint, CurvePoint curvePointToUpdate) {
    LOGGER.info("Contacting DB to update curve point...");
    if (modifiedCurvePoint.getId() != curvePointToUpdate.getId()) {
      LOGGER.warn("Your two curve points have different id. Update is not possible !");
      throw new RuntimeException("Curve point ID mismatch.");
    }
    //we only set parameters accessible from html form
    curvePointToUpdate.setCurveId(modifiedCurvePoint.getCurveId());
    curvePointToUpdate.setTerm(modifiedCurvePoint.getTerm());
    curvePointToUpdate.setValue(modifiedCurvePoint.getValue());
    return curvePointRepository.save(curvePointToUpdate);
  }
}
