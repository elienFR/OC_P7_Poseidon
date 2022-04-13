package com.openclassrooms.poseidon.controller;

import com.openclassrooms.poseidon.controller.rest.TradeRestController;
import com.openclassrooms.poseidon.domain.Trade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * This class is the controller in charge of displaying views for everything that concerns
 * trades.
 */
@Controller
@RequestMapping("/trade")
public class TradeController {

  private static final Logger LOGGER = LogManager.getLogger(TradeController.class);

  @Autowired
  private TradeRestController tradeRestController;

  /**
   * This method is used to display all trades in a list.
   *
   * @param model is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/list")
  public String home(Model model) {
    LOGGER.info("Fetching /trade/list...");
    model.addAttribute("trades", tradeRestController.getAll());
    return "trade/list";
  }

  /**
   * This method is used to add a trade in database.
   *
   * @param trade is the trade Object used to display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/add")
  public String addUser(Trade trade) {
    LOGGER.info("Fetching /trade/add...");
    return "trade/add";
  }

  /**
   * This method is used to validate a new trade before adding it to database.
   *
   * @param trade  is the trade Object in validation before add.
   * @param result is the BindingResult object that checks if errors are present in trade object.
   * @param model  is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @PostMapping("/validate")
  @Transactional
  public String validate(@Valid Trade trade, BindingResult result, Model model) {
    LOGGER.info("Fetching /trade/validate...");
    LOGGER.info("Validating entries...");
    if (!result.hasErrors()) {
      LOGGER.info("Entries validated...");
      model.addAttribute("trades", tradeRestController.getAll());
      tradeRestController.create(trade);
      return "redirect:/trade/list";
    }
    return "trade/add";
  }

  /**
   * This method is used to display the update page of an existing trade before updating it to database.
   *
   * @param id    is the trade's id that is supposed to be updated.
   * @param model is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    LOGGER.info("Fetching /trade/update/" + id);
    Trade trade = tradeRestController.getById(id).orElseThrow(
      () -> new NullPointerException("No trade found with this id (" + id + ")")
    );
    model.addAttribute("trade", trade);
    return "trade/update";
  }

  /**
   * This method is used to validate and update an existing trade into database.
   *
   * @param id     is the trade's id  that is supposed to be updated.
   * @param trade  is the trade object to update through this method.
   * @param result is the BindingResult object that checks if errors are present in trade object.
   * @param model  is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @PostMapping("/update/{id}")
  @Transactional
  public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                            BindingResult result, Model model) {
    LOGGER.info("Updating through /trade/update/" + id);
    LOGGER.info("Validating entries...");
    if (result.hasErrors()) {
      return "trade/update";
    }
    LOGGER.info("Entries validated...");
    tradeRestController.update(trade);
    model.addAttribute("trades", tradeRestController.getAll());
    return "redirect:/trade/list";
  }

  /**
   * This method is used to delete an existing trade from database.
   *
   * @param id    is the trade's id that is supposed to be deleted.
   * @param model is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/delete/{id}")
  @Transactional
  public String deleteTrade(@PathVariable("id") Integer id, Model model) {
    LOGGER.info("Fetching /trade/delete/" + id);
    tradeRestController.delete(id);
    return "redirect:/trade/list";
  }
}
