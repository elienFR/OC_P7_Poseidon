package com.openclassrooms.poseidon.domain;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "curvepoint")
public class CurvePoint {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "CurveId")
  private Integer curveId;

  private Timestamp asOfDate;

  private Double term;

  private Double value;

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
}
