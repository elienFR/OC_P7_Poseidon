package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.RuleName;
import com.openclassrooms.poseidon.repositories.RuleNameRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class RuleNameService {

  private static final Logger LOGGER = LogManager.getLogger(RuleNameService.class);

  @Autowired
  private RuleNameRepository ruleNameRepository;

  public Iterable<RuleName> getAll() {
    LOGGER.info("Contacting DB to get all rule names...");
    return ruleNameRepository.findAll();
  }

  public RuleName save(RuleName ruleName) {
    LOGGER.info("Contacting DB to save rule name...");
    if (ruleName.getId() != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        "Forbidden to save a rule name with specific id",
        new IllegalArgumentException("Forbidden to save a rule name with specific id"));
    }
    return ruleNameRepository.save(ruleName);
  }

  public Optional<RuleName> findById(Integer id) {
    LOGGER.info("Contacting DB to find rule name with id : " + id);
    return ruleNameRepository.findById(id);
  }

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

  public void delete(RuleName ruleNameToDelete) {
    LOGGER.info("Contacting DB to delete rule name : " + ruleNameToDelete.toString());
    ruleNameRepository.delete(ruleNameToDelete);
  }
}
