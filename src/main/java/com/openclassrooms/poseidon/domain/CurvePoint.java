package com.openclassrooms.poseidon.domain;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

@Entity
@Table(name = "curvepoint")
public class CurvePoint {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "curveid")
  @NotNull
  @Min(value = -128, message = "curveId must be upper than -128.")
  @Max(value = 127, message = "curveId must be less than 127.")
  private Integer curveId;

  @Column(name = "asofdate")
  private Timestamp asOfDate;

  @Column(name = "term")
  @NotNull
  @Positive(message = "term must be superior to 0")
  private Double term;

  @Column(name = "value")
  @NotNull
  private Double value;

  @Column(name = "creationdate")
  @DateTimeFormat
  private Timestamp creationDate;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getCurveId() {
    return curveId;
  }

  public void setCurveId(Integer curveId) {
    this.curveId = curveId;
  }

  public Timestamp getAsOfDate() {
    return asOfDate;
  }

  public void setAsOfDate(Timestamp asOfDate) {
    this.asOfDate = asOfDate;
  }

  public Double getTerm() {
    return term;
  }

  public void setTerm(Double term) {
    this.term = term;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public Timestamp getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Timestamp creationDate) {
    this.creationDate = creationDate;
  }

  @Override
  public String toString() {
    return "CurvePoint{" +
      "id=" + id +
      ", curveId=" + curveId +
      ", term=" + term +
      ", value=" + value +
      '}';
  }
}
