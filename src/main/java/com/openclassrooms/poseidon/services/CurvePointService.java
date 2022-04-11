package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.CurvePoint;
import com.openclassrooms.poseidon.repositories.CurvePointRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
