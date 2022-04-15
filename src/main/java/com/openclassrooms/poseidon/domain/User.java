package com.openclassrooms.poseidon.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "username")
  @NotBlank(message = "Username is mandatory")
  private String username;

  @Column(name = "password")
  @NotBlank(message = "Password is mandatory")
  private String password;

  @Column(name = "fullname")
  @NotBlank(message = "FullName is mandatory")
  private String fullname;

  @OneToMany(
    mappedBy = "user",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.EAGER
  )
  @NotEmpty(message = "Role is mandatory")
  private List<Authority> authorities = new ArrayList<>();

  @Column(name = "enabled")
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

  public List<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(List<Authority> authorities) {
    this.authorities = authorities;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", username='" + username + '\'' +
      '}';
  }

  //  -------------------------------- Helpers --------------------------------
  public void addAuthority(Authority authority) {
    authorities.add(authority);
    authority.setUser(this);
  }

  public void removeAuthority(Authority authority) {
    authorities.remove(authority);
    authority.setUser(null);
  }
}
