package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.controllers.rest.BidListRestController;
import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.services.BidListService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/bidList")
@Controller
public class BidListController {

  private static final Logger LOGGER = LogManager.getLogger(BidListController.class);
  @Autowired
  private BidListService bidListService;
  @Autowired
  private BidListRestController bidListRestController;

  @RequestMapping("/list")
  public String home(Model model) {
    LOGGER.info("Fetching /bidList/list...");
    model.addAttribute("bidLists", bidListRestController.getAll());
    return "bidList/list";
  }

  @GetMapping("/add")
  public String addBidForm(BidList bidList) {
    LOGGER.info("Fetching /bidList/add...");
    return "bidList/add";
  }

  @PostMapping("/validate")
  public String validate(@Valid BidList bid, BindingResult result, Model model) {
    LOGGER.info("Fetching /bidList/validate...");
    LOGGER.info("Validating entries...");
    if (!result.hasErrors()) {
      model.addAttribute("bidLists", bidListRestController.getAll());
      bidListRestController.create(bid);
      return "redirect:/bidList/list";
    }
    return "/bidList/add";
  }

  @GetMapping("/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    LOGGER.info("Fetching threw /bidList/update/" + id);
    BidList bidList = bidListService.findById(id).orElseThrow(
      () -> new NullPointerException("No bid found with this id (" + id + ")")
    );
    model.addAttribute("bidList", bidList);
    return "bidList/update";
  }

  @PostMapping("/update/{id}")
  public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                          BindingResult result, Model model) {
    LOGGER.info("Updating threw /bidList/update/" + id);
    if (result.hasErrors()){
      return "bidlist/update";
    }
      bidListService.save(bidList);
      model.addAttribute("bidLists", bidListRestController.getAll());
      return "redirect:/bidList/list";
  }

  @GetMapping("/delete/{id}")
  public String deleteBid(@PathVariable("id") Integer id, Model model) {
    LOGGER.info("Fetching /bidList/delete/" + id);
    bidListRestController.delete(id);
    return "redirect:/bidList/list";
  }
}
