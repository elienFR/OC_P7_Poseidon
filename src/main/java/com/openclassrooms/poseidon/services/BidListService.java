package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BidListService {

  @Autowired
  private BidListRepository bidListRepository;

  public Iterable<BidList> getAll() {
    return bidListRepository.findAll();
  }

  public void delete(Integer id) {
    BidList bidListToDelete = bidListRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Invalid bidList Id:" + id));
    bidListRepository.delete(bidListToDelete);
  }
}
