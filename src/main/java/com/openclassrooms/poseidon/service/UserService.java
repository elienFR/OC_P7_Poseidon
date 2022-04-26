package com.openclassrooms.poseidon.service;

import com.openclassrooms.poseidon.configuration.SpringSecurityConfig;
import com.openclassrooms.poseidon.domain.Authority;
import com.openclassrooms.poseidon.domain.DTO.UserDTO;
import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.domain.utils.Role;
import com.openclassrooms.poseidon.repository.AuthorityRepository;
import com.openclassrooms.poseidon.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

  private static final Logger LOGGER = LogManager.getLogger(UserService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SpringSecurityConfig springSecurityConfig;


  public Iterable<User> getAll() {
    LOGGER.info("Contacting DB to get all users...");
    return userRepository.findAll();
  }

  public Iterable<UserDTO> getAllDTO() {
    LOGGER.info("Contacting DB to get all userDTOs...");
    Iterable<User> users = getAll();
    List<UserDTO> userDTOs = new ArrayList<>();
    users.forEach(
      user -> userDTOs.add(convertUserIntoUserDTO(Optional.of(user)).get()));
    return userDTOs;
  }

  //TODO : check principal corresponds to user you save if not admin
  public User save(User user) {
    user.setPassword(
      springSecurityConfig.passwordEncoder().encode(user.getPassword())
    );
    user.setEnabled(true);
    return userRepository.save(user);
  }

  public UserDTO saveDTO(UserDTO userDTO) {
    Optional<UserDTO> optionalUserDTO = Optional.of(userDTO);
    User userToSave = convertUserDTOIntoUser(optionalUserDTO).get();
    User savedUser = save(userToSave);
    UserDTO savedUserDTO = convertUserIntoUserDTO(Optional.of(savedUser)).get();
    return savedUserDTO;
  }

  public Optional<User> findById(Integer id) {
    LOGGER.info("Contacting DB to find user with id : " + id);
    return userRepository.findById(id);
  }

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

  public UserDTO updateDTO(UserDTO modifiedUserDTO, User userFromDB) {
    //Convert user DTO into user
    User modifiedUserFromDTO = convertUserDTOIntoUser(
      Optional.of(modifiedUserDTO)
    ).get();

    Optional<User> optUpdatedUser = Optional.of(update(modifiedUserFromDTO, userFromDB));

    return convertUserIntoUserDTO(optUpdatedUser).get();
  }

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

  public void delete(User userToDelete) {
    LOGGER.info("Contacting DB to delete user : " + userToDelete.toString());
    userRepository.delete(userToDelete);
  }

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

  public Optional<UserDTO> findDTOById(Integer id) {
    return convertUserIntoUserDTO(findById(id));
  }


}
