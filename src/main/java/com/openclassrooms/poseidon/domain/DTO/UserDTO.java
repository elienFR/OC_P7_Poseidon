package com.openclassrooms.poseidon.domain.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserDTO {

  private Integer id;

  @NotBlank(message = "Username is mandatory")
  private String username;

  @NotBlank(message = "Password is mandatory")
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&-+=()\"'!?])(?=.*[0-9]).{8,20}$",
    message = "The password must contain : " +
      "at least one symbole between these : @#$%^&-+=()\"'!? " +
      "at least one number " +
      "at least one lower and one upper case letter " +
      "8 to 20 characters.")
  private String password;

  @NotBlank(message = "FullName is mandatory")
  private String fullname;

  @NotBlank(message = "Role is mandatory")
  private String role;

  private boolean enabled;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public String toString() {
    return "UserDTO{" +
      "id=" + id +
      ", username='" + username + '\'' +
      ", fullname='" + fullname + '\'' +
      ", role='" + role + '\'' +
      ", enabled=" + enabled +
      '}';
  }
}
