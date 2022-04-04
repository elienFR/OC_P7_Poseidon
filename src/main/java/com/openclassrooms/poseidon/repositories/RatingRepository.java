package com.openclassrooms.poseidon.repositories;

import com.openclassrooms.poseidon.domain.Rating;
import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<Rating, Integer> {

}
