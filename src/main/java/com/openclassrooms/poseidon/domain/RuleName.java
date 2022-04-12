package com.openclassrooms.poseidon.domain;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "rulename")
public class RuleName {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "name")
  @NotBlank(message = "Name is mandatory")
  private String name;

  @Column(name = "description")
  @NotBlank(message = "Description is mandatory")
  private String description;

  @Column(name = "json")
  @NotBlank(message = "Json is mandatory")
  private String json;

  @Column(name = "template")
  @NotBlank(message = "Template is mandatory")
  private String template;

  @Column(name = "sqlstr")
  @NotBlank(message = "SQL String is mandatory")
  private String sqlStr;

  @Column(name = "sqlpart")
  @NotBlank(message = "SQL part is mandatory")
  private String sqlPart;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public String getSqlStr() {
    return sqlStr;
  }

  public void setSqlStr(String sqlStr) {
    this.sqlStr = sqlStr;
  }

  public String getSqlPart() {
    return sqlPart;
  }

  public void setSqlPart(String sqlPart) {
    this.sqlPart = sqlPart;
  }

  @Override
  public String toString() {
    return "RuleName{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", description='" + description + '\'' +
      ", json='" + json + '\'' +
      ", template='" + template + '\'' +
      ", sqlStr='" + sqlStr + '\'' +
      ", sqlPart='" + sqlPart + '\'' +
      '}';
  }
}


