package com.openclassrooms.poseidon.domain;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

@Entity
@Table(name = "trade")
public class Trade {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tradeid")
  private Integer tradeId;

  @Column(name = "account")
  @NotBlank(message = "Account is mandatory")
  private String account;

  @Column(name = "type")
  @NotBlank(message = "type is mandatory")
  private String type;

  @Column(name = "buyquantity")
  @NotNull(message = "Buy Quantity must be superior to 0")
  @NumberFormat
  @Positive(message = "Buy Quantity must be superior to 0")
  private Double buyQuantity;

  @Column(name = "sellquantity")
  @NumberFormat
  @NotNull(message = "Sell quantity must be superior to 0")
  @Positive(message = "Sell quantity must be superior to 0")
  private Double sellQuantity;

  @Column(name = "buyprice")
  @NumberFormat
  @NotNull(message = "Buy Price must be superior to 0")
  @Positive(message = "Buy Price must be superior to 0")
  private Double buyPrice;

  @Column(name = "sellprice")
  @NumberFormat
  @NotNull(message = "Sell Price must be superior to 0")
  @Positive(message = "Sell Price must be superior to 0")
  private Double sellPrice;

  @Column(name = "benchmark")
  private String benchmark;

  @Column(name = "tradedate")
  @DateTimeFormat
  private Timestamp tradeDate;

  @Column(name = "security")
  private String security;

  @Column(name = "status")
  private String status;

  @Column(name = "trader")
  private String trader;

  @Column(name = "book")
  private String book;

  @Column(name = "creationname")
  private String creationName;

  @Column(name = "creationdate")
  @DateTimeFormat
  private Timestamp creationDate;

  @Column(name = "revisionname")
  private String revisionName;

  @Column(name = "revisiondate")
  @DateTimeFormat
  private Timestamp revisionDate;

  @Column(name = "dealname")
  private String dealName;

  @Column(name = "dealtype")
  private String dealType;

  @Column(name = "sourcelistid")
  private String sourceListId;

  @Column(name = "side")
  private String side;

  public Integer getTradeId() {
    return tradeId;
  }

  public void setTradeId(Integer tradeId) {
    this.tradeId = tradeId;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Double getBuyQuantity() {
    return buyQuantity;
  }

  public void setBuyQuantity(Double buyQuantity) {
    this.buyQuantity = buyQuantity;
  }

  public Double getSellQuantity() {
    return sellQuantity;
  }

  public void setSellQuantity(Double sellQuantity) {
    this.sellQuantity = sellQuantity;
  }

  public Double getBuyPrice() {
    return buyPrice;
  }

  public void setBuyPrice(Double buyPrice) {
    this.buyPrice = buyPrice;
  }

  public Double getSellPrice() {
    return sellPrice;
  }

  public void setSellPrice(Double sellPrice) {
    this.sellPrice = sellPrice;
  }

  public String getBenchmark() {
    return benchmark;
  }

  public void setBenchmark(String benchmark) {
    this.benchmark = benchmark;
  }

  public Timestamp getTradeDate() {
    return tradeDate;
  }

  public void setTradeDate(Timestamp tradeDate) {
    this.tradeDate = tradeDate;
  }

  public String getSecurity() {
    return security;
  }

  public void setSecurity(String security) {
    this.security = security;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTrader() {
    return trader;
  }

  public void setTrader(String trader) {
    this.trader = trader;
  }

  public String getBook() {
    return book;
  }

  public void setBook(String book) {
    this.book = book;
  }

  public String getCreationName() {
    return creationName;
  }

  public void setCreationName(String creationName) {
    this.creationName = creationName;
  }

  public Timestamp getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Timestamp creationDate) {
    this.creationDate = creationDate;
  }

  public String getRevisionName() {
    return revisionName;
  }

  public void setRevisionName(String revisionName) {
    this.revisionName = revisionName;
  }

  public Timestamp getRevisionDate() {
    return revisionDate;
  }

  public void setRevisionDate(Timestamp revisionDate) {
    this.revisionDate = revisionDate;
  }

  public String getDealName() {
    return dealName;
  }

  public void setDealName(String dealName) {
    this.dealName = dealName;
  }

  public String getDealType() {
    return dealType;
  }

  public void setDealType(String dealType) {
    this.dealType = dealType;
  }

  public String getSourceListId() {
    return sourceListId;
  }

  public void setSourceListId(String sourceListId) {
    this.sourceListId = sourceListId;
  }

  public String getSide() {
    return side;
  }

  public void setSide(String side) {
    this.side = side;
  }

  @Override
  public String toString() {
    return "Trade{" +
      "tradeId=" + tradeId +
      ", account='" + account + '\'' +
      ", type='" + type + '\'' +
      ", buyQuantity=" + buyQuantity +
      '}';
  }
}
