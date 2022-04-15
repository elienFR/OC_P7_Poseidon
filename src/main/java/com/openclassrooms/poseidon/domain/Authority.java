package com.openclassrooms.poseidon.domain;

import com.openclassrooms.poseidon.domain.utils.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "authorities")
public class Authority {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(
    fetch = FetchType.EAGER
  )
  @JoinColumn(name = "userid")
  @NotNull
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  @NotNull
  private Role role;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}
