package com.openclassrooms.poseidon.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

  @GetMapping("/logoutAfterChange")
  public String fetchSignOutSite(HttpServletRequest request, HttpServletResponse response) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }
    return "redirect:/login";
  }

}
