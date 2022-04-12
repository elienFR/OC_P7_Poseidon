package com.openclassrooms.poseidon.repository;

import com.openclassrooms.poseidon.domain.RuleName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleNameRepository extends CrudRepository<RuleName, Integer> {
}
