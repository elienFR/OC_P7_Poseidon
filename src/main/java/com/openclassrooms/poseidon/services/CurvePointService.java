package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.CurvePoint;
import com.openclassrooms.poseidon.repositories.CurvePointRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CurvePointService {

  private static final Logger LOGGER = LogManager.getLogger(CurvePointService.class);

  @Autowired
  private CurvePointRepository curvePointRepository;

  public Iterable<CurvePoint> getAll() {
    LOGGER.info("Contacting DB to get all curve points...");
    return curvePointRepository.findAll();
  }

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

  public Optional<CurvePoint> findById(Integer id) {
    LOGGER.info("Contacting DB to find curve point with id : " + id);
    return curvePointRepository.findById(id);
  }

  public void delete(CurvePoint curvePointToDelete) {
    LOGGER.info("Contacting DB to delete curve point : " + curvePointToDelete.toString());
    curvePointRepository.delete(curvePointToDelete);
  }

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
