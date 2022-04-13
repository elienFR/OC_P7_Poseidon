package com.openclassrooms.poseidon.service;

import com.openclassrooms.poseidon.configuration.SpringSecurityConfig;
import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

  //TODO : check principal corresponds to user you save if not admin
  public User save(User user) {
    user.setPassword(
      springSecurityConfig.passwordEncoder().encode(user.getPassword())
    );
    return userRepository.save(user);
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

    String encryptedPassword = springSecurityConfig.passwordEncoder()
      .encode(modifiedUser.getPassword());
    userToUpdate.setPassword(encryptedPassword);

    userToUpdate.setRole(modifiedUser.getRole());


    return userRepository.save(userToUpdate);
  }

  public void delete(User userToDelete) {
    LOGGER.info("Contacting DB to delete user : " + userToDelete.toString());
    userRepository.delete(userToDelete);
  }

}
