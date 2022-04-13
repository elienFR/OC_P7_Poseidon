package com.openclassrooms.poseidon.controller;

import com.openclassrooms.poseidon.controller.rest.UserRestController;
import com.openclassrooms.poseidon.domain.User;
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
 * users.
 */
@Controller
@RequestMapping("/user")
public class UserController {
  @Autowired
  private UserRestController userRestController;

  private Logger LOGGER = LogManager.getLogger(UserController.class);

  /**
   * This method is used to display all users in a list.
   *
   * @param model is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/list")
  public String home(Model model) {
    LOGGER.info("Fetching /user/list...");
    model.addAttribute("users", userRestController.getAll());
    return "user/list";
  }

  /**
   * This method is used to add a user in database.
   *
   * @param user is the user Object used to display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/add")
  public String addUser(User user) {
    LOGGER.info("Fetching /user/add...");
    return "user/add";
  }

  /**
   * This method is used to validate a new user before adding it to database.
   *
   * @param user   is the user Object in validation before add.
   * @param result is the BindingResult object that checks if errors are present in user object.
   * @param model  is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @PostMapping("/validate")
  @Transactional
  public String validate(@Valid User user, BindingResult result, Model model) {
    LOGGER.info("Fetching /user/validate...");
    LOGGER.info("Validating entries...");
    if (!result.hasErrors()) {
      LOGGER.info("Entries validated...");
      model.addAttribute("users", userRestController.getAll());
      userRestController.create(user);
      return "redirect:/user/list";
    }
    return "user/add";
  }

  /**
   * This method is used to display the update page of an existing user before updating it to database.
   *
   * @param id    is the user's id that is supposed to be updated.
   * @param model is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    LOGGER.info("Fetching /user/update/" + id);
    User user = userRestController.getById(id).orElseThrow(
      () -> new NullPointerException("No user found with this id (" + id + ")")
    );
    //Blank password on form page
    user.setPassword("");
    model.addAttribute("user", user);
    return "user/update";
  }

  /**
   * This method is used to validate and update an existing user into database.
   *
   * @param id     is the user's id  that is supposed to be updated.
   * @param user   is the user object to update through this method.
   * @param result is the BindingResult object that checks if errors are present in user object.
   * @param model  is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @PostMapping("/update/{id}")
  @Transactional
  public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                           BindingResult result, Model model) {
    LOGGER.info("Updating through /user/update/" + id);
    LOGGER.info("Validating entries...");
    if (result.hasErrors()) {
      return "user/update";
    }
    LOGGER.info("Entries validated...");
    userRestController.update(user);
    model.addAttribute("users", userRestController.getAll());
    return "redirect:/user/list";
  }

  /**
   * This method is used to delete an existing user from database.
   *
   * @param id    is the user's id that is supposed to be deleted.
   * @param model is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/delete/{id}")
  public String deleteUser(@PathVariable("id") Integer id, Model model) {
    LOGGER.info("Fetching /user/delete/" + id);
    userRestController.delete(id);
    return "redirect:/user/list";
  }
}
