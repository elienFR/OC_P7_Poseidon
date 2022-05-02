package com.openclassrooms.poseidon.controller;

import com.google.common.collect.Lists;
import com.openclassrooms.poseidon.controller.rest.UserRestController;
import com.openclassrooms.poseidon.domain.DTO.UserDTO;
import com.openclassrooms.poseidon.service.exception.UserAlreadyExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
  public String home(Model model,
                     Authentication authentication) {
    LOGGER.info("Fetching /user/list...");

    boolean isAdmin = isAdmin(authentication);

    List<UserDTO> users = new ArrayList<>();
    if (isAdmin) {
      users = Lists.newArrayList(userRestController.getAllUserDTO());
    } else {
      model.addAttribute("addingNotAllowed",
        "Your role does not permit you to create new user."
      );
      users.add(userRestController.getUserDTOByUserName(authentication.getName()));
    }
    model.addAttribute("users", users);
    return "user/list";
  }

  /**
   * This method is used to add a user in database.
   *
   * @param userDTO is the user Object usedDTO to display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/add")
  public String addUser(UserDTO userDTO,
                        Model model,
                        Authentication authentication) {
    LOGGER.info("Fetching /user/add...");

    boolean isAdmin = isAdmin(authentication);

    if (!isAdmin) {
      model.addAttribute("addingNotAllowed",
        "Your role does not permit you to create new user."
      );
      return "redirect:/user/list";
    }
    return "user/add";
  }

  /**
   * This method is used to validate a new user before adding it to database.
   *
   * @param userDTO is the userDTO Object in validation before add.
   * @param result  is the BindingResult object that checks if errors are present in userDTO object.
   * @param model   is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @PostMapping("/validate")
  @Transactional
  public String validate(@Valid UserDTO userDTO,
                         BindingResult result,
                         Model model,
                         Authentication authentication) {
    LOGGER.info("Fetching /user/validate...");
    LOGGER.info("Validating entries...");

    if (!result.hasErrors() && isAdmin(authentication)) {
      LOGGER.info("Entries validated...");

      model.addAttribute("users", userRestController.getAllUserDTO());

      try {
        userRestController.createUserFromDTO(userDTO);
      } catch (UserAlreadyExistsException ex) {
        model.addAttribute("userAlreadyExists", true);
        return "user/add";
      }

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
  public String showUpdateForm(@PathVariable("id") Integer id,
                               Model model, Authentication authentication) {
    LOGGER.info("Fetching /user/update/" + id);
    if (isAdmin(authentication)) {
      model.addAttribute("userIsAdmin",
        true);
    }
    UserDTO userDTO = userRestController.getDTOById(id).orElseThrow(
      () -> new NullPointerException("No user found with this id (" + id + ")")
    );
    //Blank password on form page
    userDTO.setPassword("");
    model.addAttribute("userDTO", userDTO);
    return "user/update";
  }

  /**
   * This method is used to validate and update an existing user into database.
   *
   * @param id      is the user's id  that is supposed to be updated.
   * @param userDTO is the user object to update through this method.
   * @param result  is the BindingResult object that checks if errors are present in user object.
   * @param model   is the model that display the page correctly.
   * @return is a string path where to find the view for this controller's method.
   */
  @PostMapping("/update/{id}")
  @Transactional
  public String updateUser(@PathVariable("id") Integer id,
                           @Valid UserDTO userDTO,
                           BindingResult result,
                           Model model,
                           Authentication authentication) {
    LOGGER.info("Updating through /user/update/" + id);
    LOGGER.info("Validating entries...");

    Integer authenticatedID = userRestController
      .getUserDTOByUserName(authentication.getName())
      .getId();

    if (!isAdmin(authentication)) {
      userDTO.setRole("ROLE_USER");
      if (authenticatedID != userDTO.getId()) {
        //We check that the authenticated is modifying his own user
        model.addAttribute("userMismatch",
          "You are not allowed to modify a different user than yourself !");
      }
    }

    if (result.hasErrors()) {
      return "user/update";
    }
    LOGGER.info("Entries validated...");
    userRestController.updateDTO(userDTO, authentication);

    //If the username has change, the app perform a logout
    if(!isAdmin(authentication) && !userDTO.getUsername().equals(authentication.getName())) {
      return "redirect:/logoutAfterChange";
    }

    model.addAttribute("users", userRestController.getAllUserDTO());
    return "redirect:/user/list";
  }

  /**
   * This method is used to delete an existing user from database.
   *
   * @param id is the user's id that is supposed to be deleted.
   * @return is a string path where to find the view for this controller's method.
   */
  @GetMapping("/delete/{id}")
  public String deleteUser(@PathVariable("id") Integer id, Authentication authentication) {
    LOGGER.info("Fetching /user/delete/" + id);
    Integer AuthenticationIDFromDB = userRestController
      .getUserDTOByUserName(authentication.getName())
      .getId();
    userRestController.delete(id, authentication);
    if(AuthenticationIDFromDB.equals(id)){
      return "redirect:/logoutAfterChange";
    }
    return "redirect:/user/list";
  }

  /**
   * This method checks if an authenticated user is has role admin.
   *
   * @param authentication is the authenticated user to check
   * @return true if admin
   */
  private boolean isAdmin(Authentication authentication) {
    if (authentication
      .getAuthorities()
      .stream()
      .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
    ) {
      return true;
    }
    return false;
  }
}
