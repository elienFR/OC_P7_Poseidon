package com.openclassrooms.poseidon.controller;

import com.openclassrooms.poseidon.configuration.SpringSecurityConfig;
import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

// TODO : Reprendre le code pour intégrer un UserService.
@Controller
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SpringSecurityConfig springSecurityConfig;

  private Logger LOGGER = LogManager.getLogger(UserController.class);

  @GetMapping("/user/list/index")
  public String test(){
    return "test";
  }

  @RequestMapping("/user/list")
  public String home(Model model) {
    model.addAttribute("users", userRepository.findAll());
    return "user/list";
  }

  @GetMapping("/user/add")
  public String addUser(User user) {
    LOGGER.info("Fetching /user/add...");
    return "user/add";
  }

  @PostMapping("/user/validate")
  public String validate(@Valid User user, BindingResult result, Model model) {
    LOGGER.info("Fetching /user/validate to check if user adding is valid...");
    if (!result.hasErrors()) {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      user.setPassword(encoder.encode(user.getPassword()));
      userRepository.save(user);
      model.addAttribute("users", userRepository.findAll());
      return "redirect:/user/list";
    }
    return "user/add";
  }

  // TODO : ALWAYS CHECK THAT PRINCIPAL IS THE SAME USER YOU WANT TO UPDATE
  @GetMapping("/user/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    LOGGER.info("Fetching /user/update/" + id + "...");
    User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    user.setPassword("");
    model.addAttribute("user", user);
    return "user/update";
  }

  @PostMapping("/user/update/{id}")
  public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                           BindingResult result, Model model) {
    LOGGER.info("Posting on /user/update/" + id + " to update user n°" + id + "...");
    if (result.hasErrors()) {
      return "user/update";
    }

    PasswordEncoder encoder = springSecurityConfig.passwordEncoder();
    user.setPassword(encoder.encode(user.getPassword()));
    user.setId(id);
    userRepository.save(user);
    model.addAttribute("users", userRepository.findAll());
    return "redirect:/user/list";
  }

  @GetMapping("/user/delete/{id}")
  public String deleteUser(@PathVariable("id") Integer id, Model model) {
    LOGGER.info("Fetching on /user/delete/" + id + " to delete user n°" + id + "...");
    User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    userRepository.delete(user);
    model.addAttribute("users", userRepository.findAll());
    return "redirect:/user/list";
  }
}
