package com.openclassrooms.poseidon.controllers.rest;

import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.services.BidListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bidList")
public class BidListRestController {

  @Autowired
  private BidListService bidListService;

  @GetMapping("/list")
  public Iterable<BidList> getAll() {
    return bidListService.getAll();
  }

  @DeleteMapping("/delete")
  public void delete(@RequestBody(required = true) BidList bidList) {
    bidListService.delete(bidList);
  }
}
