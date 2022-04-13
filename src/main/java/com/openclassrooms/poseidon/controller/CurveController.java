package com.openclassrooms.poseidon.controller;

import com.openclassrooms.poseidon.controller.rest.CurvePointRestController;
import com.openclassrooms.poseidon.domain.CurvePoint;
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
 * This class is the controller in charge of displaying views for everything
 * that concerns curve point.
 */
@Controller
@RequestMapping("/curvePoint")
public class CurveController {

  private static final Logger LOGGER = LogManager.getLogger(BidListController.class);

  @Autowired
  private CurvePointRestController curvePointRestController;

  /**
   * This method is used to display all curve points in a list.
   *
   * @param model is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/list")
  public String home(Model model) {
    LOGGER.info("Fetching /curvePoint/list...");
    model.addAttribute("curvePoints", curvePointRestController.getAll());
    return "curvePoint/list";
  }

  /**
   * This method is used to add a curve point in database.
   *
   * @param curvePoint is the curve point Object used to display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/add")
  public String addBidForm(CurvePoint curvePoint) {
    LOGGER.info("Fetching /curvePoint/add...");
    return "curvePoint/add";
  }

  /**
   * This method is used to validate a new curve point before adding it to database.
   *
   * @param curvePoint is the curve point Object in validation before add.
   * @param result     is the BindingResult object that checks if errors are present in curve point object.
   * @param model      is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @PostMapping("/validate")
  @Transactional
  public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
    LOGGER.info("Fetching /curvePoint/validate...");
    LOGGER.info("Validating entries...");
    if (!result.hasErrors()) {
      LOGGER.info("Entries validated...");
      model.addAttribute("curvePoints", curvePointRestController.getAll());
      curvePointRestController.create(curvePoint);
      return "redirect:/curvePoint/list";
    }
    return "curvePoint/add";
  }

  /**
   * This method is used to display the update page of an existing curve point before updating it to database.
   *
   * @param id    is the curve point's id that is supposed to be updated.
   * @param model is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    LOGGER.info("Fetching threw /curvePoint/update/" + id);
    CurvePoint curvePoint = curvePointRestController.getById(id).orElseThrow(
      () -> new NullPointerException("No curve point found with this id (" + id + ")")
    );
    model.addAttribute("curvePoint", curvePoint);
    return "curvePoint/update";
  }

  /**
   * This method is used to validate and update an existing curve point into database.
   *
   * @param id         is the curve point's id  that is supposed to be updated.
   * @param curvePoint is the curve point object to update through this method.
   * @param result     is the BindingResult object that checks if errors are present in curve point object.
   * @param model      is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @PostMapping("/update/{id}")
  @Transactional
  public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                          BindingResult result, Model model) {
    LOGGER.info("Updating through /curvePoint/update/" + id);
    LOGGER.info("Validating entries...");
    if (result.hasErrors()) {
      return "curvePoint/update";
    }
    LOGGER.info("Entries validated...");
    curvePointRestController.update(curvePoint);
    model.addAttribute("curvePoints", curvePointRestController.getAll());
    return "redirect:/curvePoint/list";
  }

  /**
   * This method is used to delete an existing curve point from database.
   *
   * @param id    is the curve point's id that is supposed to be deleted.
   * @param model is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/delete/{id}")
  @Transactional
  public String deleteBid(@PathVariable("id") Integer id, Model model) {
    LOGGER.info("Fetching /curvePoint/delete/" + id);
    curvePointRestController.delete(id);
    return "redirect:/curvePoint/list";
  }
}
