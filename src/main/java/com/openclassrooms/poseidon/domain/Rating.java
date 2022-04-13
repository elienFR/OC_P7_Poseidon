package com.openclassrooms.poseidon.domain;


import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "rating")
public class Rating {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "moodysrating")
  @NotBlank(message = "Moody's rating is mandatory.")
  private String moodysRating;

  @Column(name = "sandprating")
  @NotBlank(message = "Standard And Poor's rating is mandatory.")
  private String sandPRating;

  @Column(name = "fitchrating")
  @NotBlank(message = "Fitch rating is mandatory.")
  private String fitchRating;

  @Column(name = "ordernumber")
  @NotNull
  @Min(value = -128, message = "Order number must be upper than -128.")
  @Max(value = 127, message = "Order number must be less than 127.")
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

  @Override
  public String toString() {
    return "Rating{" +
      "id=" + id +
      ", moodysRating='" + moodysRating + '\'' +
      ", sandPRating='" + sandPRating + '\'' +
      ", fitchRating='" + fitchRating + '\'' +
      ", orderNumber=" + orderNumber +
      '}';
  }
}
