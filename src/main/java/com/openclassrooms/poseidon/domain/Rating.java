package com.openclassrooms.poseidon.domain;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "rating")
public class Rating {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
//  @NotBlank(message = "MoodysRating is mandatory")
  private String moodysRating;
//  @NotBlank(message = "SandPRating is mandatory")
  private String sandPRating;
//  @NotBlank(message = "FitchRating is mandatory")
  private String fitchRating;
//  @NotBlank(message = "OrderNumber is mandatory")
  private Integer orderNumber;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getMoodysRating() {
    return moodysRating;
  }

  public void setMoodysRating(String moodysRating) {
    this.moodysRating = moodysRating;
  }

  public String getSandPRating() {
    return sandPRating;
  }

  public void setSandPRating(String sandPRating) {
    this.sandPRating = sandPRating;
  }

  public String getFitchRating() {
    return fitchRating;
  }

  public void setFitchRating(String fitchRating) {
    this.fitchRating = fitchRating;
  }

  public Integer getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(Integer orderNumber) {
    this.orderNumber = orderNumber;
  }
}
