package com.openclassrooms.poseidon.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openclassrooms.poseidon.domain.RuleName;
import com.openclassrooms.poseidon.domain.Trade;
import com.openclassrooms.poseidon.service.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "springadmin", roles = {"ADMIN", "USER"})
public class TradeResControllerUnitTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TradeService tradeServiceMocked;

  @Value("${api.ver}")
  private String apiVer;

  private String baseUrl;

  private Trade givenTrade;

  private Gson gson
    = new GsonBuilder()
    .setPrettyPrinting()
    .create();

  private String jsonOfGivenTrade;

  @BeforeEach
  public void setUp() {
    //URL base setup
    baseUrl = "/" + apiVer + "/trade";

    String givenAccount = "someAccount";
    String givenType = "someType";
    Double givenBuyQuantity = 1.0d;
    givenTrade = new Trade();
    givenTrade.setAccount(givenAccount);
    givenTrade.setType(givenType);
    givenTrade.setBuyQuantity(givenBuyQuantity);

    jsonOfGivenTrade = gson.toJson(givenTrade);
  }

  @WithMockUser(username = "sringuser")
  @Test
  public void getAllUnauthorizedTest() throws Exception {
    mockMvc.perform(get(baseUrl + "/list"))
      .andExpect(status().isForbidden());
    verify(tradeServiceMocked, times(0)).getAll();
  }

  @Test
  public void getAllTest() throws Exception {
    mockMvc.perform(get(baseUrl + "/list"))
      .andExpect(status().isOk());
    verify(tradeServiceMocked, times(1)).getAll();
  }

  @Test
  public void getByIdTest() throws Exception {
    Integer givenInteger = 1;
    mockMvc.perform(get(baseUrl + "/list/" + givenInteger))
      .andExpect(status().isOk());
    verify(tradeServiceMocked, times(1)).findById(1);
  }

  @Test
  public void deleteTest() throws Exception {
    when(tradeServiceMocked.findById(1)).thenReturn(Optional.of(new Trade()));
    mockMvc.perform(
        delete(baseUrl + "/delete").param("id","1")
      )
      .andExpect(status().isOk());
    verify(tradeServiceMocked, times(1)).findById(1);
  }

  @Test
  public void deleteTestWithNotFound() throws Exception {
    when(tradeServiceMocked.findById(1)).thenReturn(null);
    mockMvc.perform(
        delete(baseUrl + "/delete").param("id","1")
      )
      .andExpect(status().isNotFound());
    verify(tradeServiceMocked, times(0)).delete(any(Trade.class));
  }

  @Test
  public void createTest() throws Exception {
    when(tradeServiceMocked.save(any(Trade.class))).thenReturn(givenTrade);
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonOfGivenTrade)
      )
      .andExpect(status().isOk());
    verify(tradeServiceMocked,times(1)).save(any(Trade.class));
  }

  @Test
  public void createBadBodyTest() throws Exception {
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content("")
      )
      .andExpect(status().isBadRequest());
    verify(tradeServiceMocked,times(0)).save(any(Trade.class));
  }

  @Test
  public void createNotValidTest() throws Exception {
    givenTrade.setAccount(null);
    jsonOfGivenTrade = gson.toJson(givenTrade);
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonOfGivenTrade)
      )
      .andExpect(status().isBadRequest());
    verify(tradeServiceMocked,times(0)).save(any(Trade.class));
  }

  @Test
  public void updateBadBodyTest() throws Exception {
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content("")
    ).andExpect(status().isBadRequest());
    verify(tradeServiceMocked,times(0)).update(any(Trade.class),any(Trade.class));
  }

  @Test
  public void updateWithNotFoundBidListTest() throws Exception {
    when(tradeServiceMocked.findById(28)).thenReturn(null);
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonOfGivenTrade)
    ).andExpect(status().isNotFound());
    verify(tradeServiceMocked,times(0)).update(any(Trade.class),any(Trade.class));
  }

  @Test
  public void updateTest() throws Exception {
    givenTrade.setTradeId(28);
    jsonOfGivenTrade = gson.toJson(givenTrade);
    when(tradeServiceMocked.findById(any(Integer.class))).thenReturn(Optional.of(givenTrade));
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonOfGivenTrade)
    ).andExpect(status().isOk());
    verify(tradeServiceMocked,times(1)).update(any(Trade.class),any(Trade.class));
  }

}
