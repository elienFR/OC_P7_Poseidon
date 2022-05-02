package com.openclassrooms.poseidon.controller.rest;

import com.openclassrooms.poseidon.domain.DTO.UserDTO;
import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.service.UserService;
import com.openclassrooms.poseidon.service.exception.UserAlreadyExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class is the REST Controller to communicate with database through JSON files
 * with user objects.
 */
@RestController
@RequestMapping("${api.ver}" + "/user")
@RolesAllowed("ROLE_ADMIN")
public class UserRestController {

  private static final Logger LOGGER = LogManager.getLogger(UserRestController.class);

  @Autowired
  private UserService userService;


  /**
   * this method is one of the Exception Handler to display specific exceptions from
   * this controller.
   *
   * @param ex is the exception handled here
   * @return a map with all errors stacked in this exception.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(
    MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(
      (error) -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
      }
    );
    return errors;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public String handleValidationExceptions(
    IllegalArgumentException ex) {
    return ex.getLocalizedMessage();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public String handleValidationExceptions(
    HttpMessageNotReadableException ex) {
    return "The json you provided cannot be parsed. Some variable does not match the scheme.";
  }

  /**
   * this method is one of the Exception Handler to display specific exceptions from
   * this controller.
   *
   * @param ex is the exception handled here
   * @return a string with the error contained in this exception.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NullPointerException.class)
  public String handleValidationExceptions(
    NullPointerException ex) {
    return ex.getLocalizedMessage();
  }

  /**
   * This method is used to display all userDTOs in a list.
   *
   * @return the iterable of all userDTOs contained in database.
   */
  @GetMapping("/list")
  public Iterable<UserDTO> getAllUserDTO() {
    LOGGER.info("API Request -> get all users...");
    return userService.getAllDTO();
  }

  /**
   * This method is used to add a new userDTO into database.
   *
   * @param userDTO is the Json body of the userDTO you want to add.
   * @return the confirmation message that you correctly added the bid.
   */
  @PostMapping("/add")
  @Transactional
  public ResponseEntity<String> createUserFromDTO(@Valid @RequestBody UserDTO userDTO) throws UserAlreadyExistsException {
    LOGGER.info("API Request -> saving userDTO : ");
    LOGGER.info(userDTO.toString());
    UserDTO savedUserDTO = userService.saveDTO(userDTO);
    LOGGER.info("User saved successfully");
    return ResponseEntity.ok("Successfully created with id : " + savedUserDTO.getId() + ".");

  }

  /**
   * This method is used to update an existing user from database.
   *
   * @param userDTO is the userDTO you want to modify.
   * @return the confirmation message that you correctly updated the userDTO.
   */
  @PutMapping("/update")
  @Transactional
  public ResponseEntity<String> updateDTO(@Valid @RequestBody UserDTO userDTO, Authentication authentication) {
    LOGGER.info("API Request -> updating userDTO : ");
    LOGGER.info(userDTO.toString());

    Integer authenticatedID = userService
      .getUserDTOByUserName(authentication.getName())
      .get()
      .getId();

    if (!isAdmin(authentication) && authenticatedID != userDTO.getId()) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        "You are not allowed to modify another account than yours."
      );
    }

    User userFromDB = userService.findById(userDTO.getId()).orElseThrow(
      () -> new NullPointerException("No user found with this id (" + userDTO.getId() + ")")
    );
    userService.updateDTO(userDTO, userFromDB);
    LOGGER.info("User updated successfully with its DTO !");
    return ResponseEntity.ok("User successfully updated with its DTO !");
  }

  /**
   * This method is used to delete a user from database.
   *
   * @param id is the id of the user you want to delete.
   * @return the confirmation message of deletion.
   */
  @DeleteMapping("/delete")
  @Transactional
  public ResponseEntity<String> delete(@RequestParam Integer id, Authentication authentication) {
    LOGGER.info("API Request -> deleting user with id : " + id);
    User userToDelete = userService.findById(id)
      .orElseThrow(() -> new NullPointerException("No user with id " + id + " exists in DB."));

    if (!isAdmin(authentication)
      && !userToDelete.getId().equals(getUserDTOByUserName(authentication.getName()).getId())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        "You are not allowed to delete this user"
      );
    }
    userService.delete(userToDelete);
    return ResponseEntity.ok("User with id " + id + " has been deleted successfully.");
  }

  /**
   * This method is used to recover a specific userdto thanks to its id.
   *
   * @param id is the userdto's id you are looking for.
   * @return an optional user object
   */
  @GetMapping("/list/{id}")
  public Optional<UserDTO> getDTOById(@PathVariable Integer id) {
    LOGGER.info("API Request -> get userDTO with Id : " + id + "...");
    return userService.findDTOById(id);
  }


  /**
   * This method retrieve a userDTO thanks to its username.
   *
   * @param username is the username chosen to recover the userDTO
   * @return a userDTO object corresponding to the username if present in DB.
   * @throws ResponseStatusException with a HttpStatus.NOT_FOUND if no userDTO is found.
   */
  public UserDTO getUserDTOByUserName(String username) {
    Optional<UserDTO> optionalUserDTO = userService.getUserDTOByUserName(username);
    if (optionalUserDTO.isPresent()) {
      return optionalUserDTO.get();
    } else {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "No user found with this username : " + username
      );
    }
  }

  /**
   * This method check if a user exists thanks to its username.
   *
   * @param username is the username to check
   * @return true if it exists
   */
  public boolean existsByUsername(String username) {
    return userService.existsByUsername(username);
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
