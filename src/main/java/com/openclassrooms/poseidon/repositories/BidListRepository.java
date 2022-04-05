package com.openclassrooms.poseidon.repositories;

import com.openclassrooms.poseidon.domain.BidList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidListRepository extends CrudRepository<BidList, Integer> {

}
