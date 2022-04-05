package com.openclassrooms.poseidon.repositories;

import com.openclassrooms.poseidon.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

}
