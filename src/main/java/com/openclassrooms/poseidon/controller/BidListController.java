package com.openclassrooms.poseidon.controller;

import com.openclassrooms.poseidon.controller.rest.BidListRestController;
import com.openclassrooms.poseidon.domain.BidList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;

/**
 * This class is the controller in charge of displaying views for everything that concerns bids.
 */
@RequestMapping("/bidList")
@Controller
public class BidListController {

  private static final Logger LOGGER = LogManager.getLogger(BidListController.class);
  @Autowired
  private BidListRestController bidListRestController;

  /**
   * This method is used to display all bids in a list.
   *
   * @param model is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/list")
  public String home(Principal principal, Model model) {
    LOGGER.info("Fetching /bidList/list...");
    model.addAttribute("bidLists", bidListRestController.getAll());
    return "bidList/list";
  }

  /**
   * This method is used to add a bid in database.
   *
   * @param bidList is the bidList Object used to display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/add")
  public String addBidForm(BidList bidList) {
    LOGGER.info("Fetching /bidList/add...");
    return "bidList/add";
  }

  /**
   * This method is used to validate a new bid before adding it to database.
   *
   * @param bid is the bidList Object in validation before add.
   * @param result is the BindingResult object that checks if errors are present in bid object.
   * @param model is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @PostMapping("/validate")
  @Transactional
  public String validate(@Valid BidList bid, BindingResult result, Model model) {
    LOGGER.info("Fetching /bidList/validate...");
    LOGGER.info("Validating entries...");
    if (!result.hasErrors()) {
      LOGGER.info("Entries validated...");
      model.addAttribute("bidLists", bidListRestController.getAll());
      bidListRestController.create(bid);
      return "redirect:/bidList/list";
    }
    return "/bidList/add";
  }

  /**
   * This method is used to display the update page of an existing bid before updating it to database.
   *
   * @param id is the bidList's id that is supposed to be updated.
   * @param model is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    LOGGER.info("Fetching threw /bidList/update/" + id);
    BidList bidList = bidListRestController.getById(id).orElseThrow(
      () -> new NullPointerException("No bid found with this id (" + id + ")")
    );
    model.addAttribute("bidList", bidList);
    return "bidList/update";
  }

  /**
   * This method is used to validate and update an existing bid into database.
   *
   * @param id is the bidList's id  that is supposed to be updated.
   * @param bidList is the BidList object to update through this method.
   * @param result is the BindingResult object that checks if errors are present in bid object.
   * @param model is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @PostMapping("/update/{id}")
  @Transactional
  public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                          BindingResult result, Model model) {
    LOGGER.info("Updating through /bidList/update/" + id);
    LOGGER.info("Validating entries...");
    if (result.hasErrors()){
      return "bidlist/update";
    }
    LOGGER.info("Entries validated...");
    bidListRestController.update(bidList);
      model.addAttribute("bidLists", bidListRestController.getAll());
      return "redirect:/bidList/list";
  }

  /**
   * This method is used to delete an existing bid from database.
   *
   * @param id is the bidList's id that is supposed to be deleted.
   * @param model is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/delete/{id}")
  @Transactional
  public String deleteBid(@PathVariable("id") Integer id, Model model) {
    LOGGER.info("Fetching /bidList/delete/" + id);
    bidListRestController.delete(id);
    return "redirect:/bidList/list";
  }
}
