package com.openclassrooms.poseidon.repositories;

import com.openclassrooms.poseidon.domain.Rating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Integer> {

}
