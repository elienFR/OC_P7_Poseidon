package com.openclassrooms.poseidon.repositories;

import com.openclassrooms.poseidon.domain.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Integer> {

}
