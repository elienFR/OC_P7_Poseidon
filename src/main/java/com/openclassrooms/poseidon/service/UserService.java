package com.openclassrooms.poseidon.service;

import com.openclassrooms.poseidon.configuration.SpringSecurityConfig;
import com.openclassrooms.poseidon.domain.Authority;
import com.openclassrooms.poseidon.domain.DTO.UserDTO;
import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.domain.utils.Role;
import com.openclassrooms.poseidon.repository.AuthorityRepository;
import com.openclassrooms.poseidon.repository.UserRepository;
import com.openclassrooms.poseidon.service.exception.UserAlreadyExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * This class is a CRUD service for user objects
 *
 */
@Service
@Transactional
public class UserService {

  private static final Logger LOGGER = LogManager.getLogger(UserService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SpringSecurityConfig springSecurityConfig;

  /**
   * This method is used to display all user in iterables.
   *
   * @return is an iterable containing all user Object.
   */
  public Iterable<User> getAll() {
    LOGGER.info("Contacting DB to get all users...");
    return userRepository.findAll();
  }

  /**
   * This method is used to display all userDTO in iterables.
   *
   * @return is an iterable containing all user Object.
   */
  public Iterable<UserDTO> getAllDTO() {
    LOGGER.info("Contacting DB to get all userDTOs...");
    Iterable<User> users = getAll();
    List<UserDTO> userDTOs = new ArrayList<>();
    users.forEach(
      user -> userDTOs.add(convertUserIntoUserDTO(Optional.of(user)).get()));
    return userDTOs;
  }

  /**
   * This method is used to save a NEW user in the database, and it also adds the creation
   * date to the object.
   * It checks that the object you want to save DOES have a null id.
   * This null property means the object does not exist in DB.
   * The password is hashed during the execution of this method.
   *
   * @param user is the user object you want to save.
   * @return the saved user
   */
  public User save(User user) {
    if(user.getId()==null && existsByUserName(user)){
      throw new RuntimeException("The user already exists with this username");
    }
    user.setPassword(
      springSecurityConfig.passwordEncoder().encode(user.getPassword())
    );
    user.setEnabled(true);
    return userRepository.save(user);
  }

  /**
   * This method is used to save a NEW userDTO in the database.
   * It checks that the object you want to save DOES have a null id.
   * This null property means the object does not exist in DB.
   * The password is hashed during the execution of this method.
   *
   * @param userDTO is the user object you want to save.
   * @return the saved user
   */
  public UserDTO saveDTO(UserDTO userDTO) throws UserAlreadyExistsException {
    Optional<UserDTO> optionalUserDTO = Optional.of(userDTO);
    User userToSave = convertUserDTOIntoUser(optionalUserDTO).get();
    if(userToSave.getId()==null && existsByUserName(userToSave)){
      throw new UserAlreadyExistsException("The user already exists with this username");
    }
    User savedUser = save(userToSave);
    UserDTO savedUserDTO = convertUserIntoUserDTO(Optional.of(savedUser)).get();
    return savedUserDTO;
  }

  /**
   * This method is used to check the existence of a user in DB thanks to its username.
   *
   * @param user is the user you want to check.
   * @return true if the user exists.
   */
  private boolean existsByUserName(User user) {
    LOGGER.info("Checking user existence through its username : " + user.getUsername() + "...");
    return userRepository.existsByUsername(user.getUsername());
  }

  /**
   * This method is used to find a specific user object from DB thanks to its id.
   *
   * @param id is the id of the user you want to retrieve form DB.
   * @return an optional user Object.
   */
  public Optional<User> findById(Integer id) {
    LOGGER.info("Contacting DB to find user with id : " + id);
    return userRepository.findById(id);
  }

  /**
   * This method is used to update an EXISTING user object.
   *
   * @param modifiedUser is the object that is going to overwrite the one in DB.
   * @param userToUpdate is the object from db to be updated. (the repo that connect to db will basically just check this object id).
   * @return the updated user object
   */
  public User update(User modifiedUser, User userToUpdate) {
    LOGGER.info("Contacting DB to update user...");
    if (modifiedUser.getId() != userToUpdate.getId()) {
      LOGGER.warn("Your two users have different id. Update is not possible !");
      throw new RuntimeException("Users ID mismatch.");
    }
    //we only set parameters accessible from html form
    userToUpdate.setFullname(modifiedUser.getFullname());
    userToUpdate.setUsername(modifiedUser.getUsername());
    userToUpdate.setPassword(modifiedUser.getPassword());

    userToUpdate.setAuthorities(modifiedUser.getAuthorities());

    return userRepository.save(userToUpdate);
  }

  /**
   * This method is used to update an EXISTING userDTO object.
   *
   * @param modifiedUserDTO is the object that is going to overwrite the one in DB.
   * @param userFromDB is the object from db to be updated. (the repo that connect to db will basically just check this object id).
   * @return the updated userDTO object
   */
  public UserDTO updateDTO(UserDTO modifiedUserDTO, User userFromDB) {
    //Convert user DTO into user
    User modifiedUserFromDTO = convertUserDTOIntoUser(
      Optional.of(modifiedUserDTO)
    ).get();

    Optional<User> optUpdatedUser = Optional.of(update(modifiedUserFromDTO, userFromDB));

    return convertUserIntoUserDTO(optUpdatedUser).get();
  }

  /**
   *
   * This method convert a UserDTO object into a User object.
   *
   * @param optUserDtoToConvert is the optional UserDTO object to convert.
   * @return an optional user Object that corresponds to the userDTO input.
   */
  private Optional<User> convertUserDTOIntoUser(Optional<UserDTO> optUserDtoToConvert) {
    if (optUserDtoToConvert.isPresent()) {
      UserDTO userDtoToConvert = optUserDtoToConvert.get();
      User user;
      if (userDtoToConvert.getId() != null) {
        user = findById(userDtoToConvert.getId()).orElse(new User());
      } else {
        user = new User();
      }
      user.setUsername(userDtoToConvert.getUsername());
      user.setPassword(userDtoToConvert.getPassword());
      user.setEnabled(userDtoToConvert.isEnabled());
      user.setFullname(userDtoToConvert.getFullname());

      //For authorities, we check database first
      List<String> authorities = new ArrayList<>();
      user.getAuthorities().forEach(authority -> authorities.add(authority.getRole().toString()));

      if (userDtoToConvert.getRole().equals(Role.ROLE_ADMIN.toString())) {
        if (!authorities.contains(Role.ROLE_ADMIN.toString())) {
          Authority adminAuthority = new Authority();
          adminAuthority.setRole(Role.ROLE_ADMIN);
          user.addAuthority(adminAuthority);
        }
        if (!authorities.contains(Role.ROLE_USER.toString())) {
          Authority userAuthority = new Authority();
          userAuthority.setRole(Role.ROLE_USER);
          user.addAuthority(userAuthority);
        }
      } else if (userDtoToConvert.getRole().equals(Role.ROLE_USER.toString())) {
        if (!authorities.contains(Role.ROLE_USER.toString())) {
          Authority userAuthority = new Authority();
          userAuthority.setRole(Role.ROLE_USER);
          user.addAuthority(userAuthority);
        }
        if (authorities.contains(Role.ROLE_ADMIN.toString())) {
          Authority authorityAdminToDelete = new Authority();
          for (Authority authority : user.getAuthorities()) {
            if (authority.getRole().equals(Role.ROLE_ADMIN)) {
              authorityAdminToDelete = authority;
            }
          }
          user.removeAuthority(authorityAdminToDelete);
        }
      }
      return Optional.of(user);
    } else {
      return Optional.of(new User());
    }

  }

  /**
   * This method is used to delete an existing user from database.
   *
   * @param userToDelete is the deleted user.
   */
  public void delete(User userToDelete) {
    LOGGER.info("Contacting DB to delete user : " + userToDelete.toString());
    userRepository.delete(userToDelete);
  }

  /**
   *
   * This method convert a User object into a UserDTO object.
   *
   * @param optUserToConvert is the Optional User object to convert.
   * @return an optional UserDTO Object that corresponds to the user input.
   */
  private Optional<UserDTO> convertUserIntoUserDTO(Optional<User> optUserToConvert) {
    if (optUserToConvert.isPresent()) {
      User userToConvert = optUserToConvert.get();

      UserDTO userDTO = new UserDTO();
      userDTO.setId(userToConvert.getId());
      userDTO.setUsername(userToConvert.getUsername());
      userDTO.setPassword(userToConvert.getPassword());
      userDTO.setFullname(userToConvert.getFullname());
      userDTO.setEnabled(userToConvert.isEnabled());

      List<String> authorities = new ArrayList<>();
      userToConvert.getAuthorities().forEach(authority -> authorities.add(authority.getRole().toString()));

      if (authorities.contains(Role.ROLE_ADMIN.toString())) {
        userDTO.setRole(Role.ROLE_ADMIN.toString());
      } else {
        userDTO.setRole(Role.ROLE_USER.toString());
      }
      Optional<UserDTO> optUserDTO = Optional.of(userDTO);
      return optUserDTO;

    } else {
      return Optional.of(new UserDTO());
    }
  }

  /**
   * This method is used to find a specific userDTO object from DB thanks to its id.
   *
   * @param id is the id of the userDTO you want to retrieve form DB.
   * @return an optional userDTO Object.
   */
  public Optional<UserDTO> findDTOById(Integer id) {
    return convertUserIntoUserDTO(findById(id));
  }


}
