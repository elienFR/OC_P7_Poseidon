package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class BidListService {

  @Autowired
  private BidListRepository bidListRepository;

  public Iterable<BidList> getAll() {
    return bidListRepository.findAll();
  }

  public void delete(BidList bidList) {
    bidListRepository.delete(bidList);
  }

  public Optional<BidList> findById(Integer id) {
    return bidListRepository.findById(id);
  }
}
