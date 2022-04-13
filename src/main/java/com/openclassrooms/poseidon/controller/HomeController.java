package com.openclassrooms.poseidon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {
  @GetMapping("/")
  public String home(Principal principal, Model model) {
    return "home";
  }

  @GetMapping("/home")
  public String bidListHome(Model model) {
    return "redirect:/bidList/list";
  }


}
