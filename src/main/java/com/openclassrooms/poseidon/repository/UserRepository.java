package com.openclassrooms.poseidon.repository;

import com.openclassrooms.poseidon.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

  boolean existsByUsername(String username);
}
