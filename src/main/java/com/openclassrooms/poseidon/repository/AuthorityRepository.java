package com.openclassrooms.poseidon.repository;

import com.openclassrooms.poseidon.domain.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Integer> {
}
