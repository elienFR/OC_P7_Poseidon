package com.openclassrooms.poseidon.service;

import com.openclassrooms.poseidon.domain.RuleName;
import com.openclassrooms.poseidon.repository.RuleNameRepository;
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
 * This class is a CRUD service for RuleName objects
 *
 */
@Service
@Transactional
public class RuleNameService {

  private static final Logger LOGGER = LogManager.getLogger(RuleNameService.class);

  @Autowired
  private RuleNameRepository ruleNameRepository;

  /**
   * This method is used to display all ruleName in iterables.
   *
   * @return is an iterable containing all RuleName Object.
   */
  public Iterable<RuleName> getAll() {
    LOGGER.info("Contacting DB to get all rule names...");
    return ruleNameRepository.findAll();
  }

  /**
   * This method is used to save a NEW RuleName in the database.
   * It checks that the object you want to save DOES have a null id.
   * This null property means the object does not exist in DB.
   *
   * @param ruleName is the ruleName object you want to save.
   * @return the saved ruleName
   */
  public RuleName save(RuleName ruleName) {
    LOGGER.info("Contacting DB to save rule name...");
    if (ruleName.getId() != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        "Forbidden to save a rule name with specific id",
        new IllegalArgumentException("Forbidden to save a rule name with specific id"));
    }
    return ruleNameRepository.save(ruleName);
  }


  /**
   * This method is used to find a specific ruleName object from DB thanks to its id.
   *
   * @param id is the id of the ruleName you want to retrieve form DB.
   * @return an optional RuleName Object.
   */
  public Optional<RuleName> findById(Integer id) {
    LOGGER.info("Contacting DB to find rule name with id : " + id);
    return ruleNameRepository.findById(id);
  }

  /**
   * This method is used to update an EXISTING ruleName object.
   *
   * @param modifiedRuleName is the object that is going to overwrite the one in DB.
   * @param ruleNameToUpdate is the object from db to be updated. (the repo that connect to db will basically just check this object id).
   * @return the updated ruleName object
   */
  public RuleName update(RuleName modifiedRuleName, RuleName ruleNameToUpdate) {
    LOGGER.info("Contacting DB to update rule name...");
    if (modifiedRuleName.getId() != ruleNameToUpdate.getId()) {
      LOGGER.warn("Your two rule names have different id. Update is not possible !");
      throw new RuntimeException("Rule names ID mismatch.");
    }
    //we only set parameters accessible from html form
    ruleNameToUpdate.setName(modifiedRuleName.getName());
    ruleNameToUpdate.setDescription(modifiedRuleName.getDescription());
    ruleNameToUpdate.setJson(modifiedRuleName.getJson());
    ruleNameToUpdate.setTemplate(modifiedRuleName.getTemplate());
    ruleNameToUpdate.setSqlStr(modifiedRuleName.getSqlStr());
    ruleNameToUpdate.setSqlPart(modifiedRuleName.getSqlPart());

    return ruleNameRepository.save(ruleNameToUpdate);
  }

  /**
   * This method is used to delete an existing ruleName from database.
   *
   * @param ruleNameToDelete is the deleted ruleName.
   */
  public void delete(RuleName ruleNameToDelete) {
    LOGGER.info("Contacting DB to delete rule name : " + ruleNameToDelete.toString());
    ruleNameRepository.delete(ruleNameToDelete);
  }
}
