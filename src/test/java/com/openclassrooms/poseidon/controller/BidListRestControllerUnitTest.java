package com.openclassrooms.poseidon.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.service.BidListService;
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

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql({"/test-schema.sql", "/test-data.sql"})
@WithMockUser(username = "springadmin", roles = {"ADMIN", "USER"})
public class BidListRestControllerUnitTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BidListService bidListServiceMocked;

  @Value("${api.ver}")
  private String apiVer;

  private String baseUrl;

  private BidList givenBidList;

  private Gson gson
    = new GsonBuilder()
    .setPrettyPrinting()
    .create();

  private String jsonOfGivenBidList;

  @BeforeEach
  public void setUp() {
    //URL base setup
    baseUrl = "/" + apiVer + "/bidList";

    givenBidList = new BidList();
    givenBidList.setAccount("someAccount");
    givenBidList.setType("someType");
    givenBidList.setBidQuantity(28.0d);

    jsonOfGivenBidList = gson.toJson(givenBidList);
  }

  @WithMockUser(username = "sringuser")
  @Test
  public void getAllUnauthorizedTest() throws Exception {
    mockMvc.perform(get(baseUrl + "/list"))
      .andExpect(status().isForbidden());
    verify(bidListServiceMocked, times(0)).getAll();
  }

  @Test
  public void getAllTest() throws Exception {
    mockMvc.perform(get(baseUrl + "/list"))
      .andExpect(status().isOk());
    verify(bidListServiceMocked, times(1)).getAll();
  }

  @Test
  public void getByIdTest() throws Exception {
    Integer givenInteger = 1;
    mockMvc.perform(get(baseUrl + "/list/" + givenInteger))
      .andExpect(status().isOk());
    verify(bidListServiceMocked, times(1)).findById(1);
  }

  @Test
  public void deleteTest() throws Exception {
    when(bidListServiceMocked.findById(1)).thenReturn(Optional.of(new BidList()));
    mockMvc.perform(
        delete(baseUrl + "/delete").param("id","1")
      )
      .andExpect(status().isOk());
    verify(bidListServiceMocked, times(1)).findById(1);
  }

  @Test
  public void deleteTestWithNotFound() throws Exception {
    when(bidListServiceMocked.findById(1)).thenReturn(null);
    mockMvc.perform(
        delete(baseUrl + "/delete").param("id","1")
      )
      .andExpect(status().isNotFound());
    verify(bidListServiceMocked, times(0)).delete(any(BidList.class));
  }

  @Test
  public void createTest() throws Exception {
    when(bidListServiceMocked.save(any(BidList.class))).thenReturn(givenBidList);
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonOfGivenBidList)
      )
      .andExpect(status().isOk());
    verify(bidListServiceMocked,times(1)).save(any(BidList.class));
  }

  @Test
  public void createBadBodyTest() throws Exception {
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content("")
      )
      .andExpect(status().isBadRequest());
    verify(bidListServiceMocked,times(0)).save(any(BidList.class));
  }

  @Test
  public void createNotValidTest() throws Exception {
    givenBidList.setAccount(null);
    jsonOfGivenBidList = gson.toJson(givenBidList);
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonOfGivenBidList)
      )
      .andExpect(status().isBadRequest());
    verify(bidListServiceMocked,times(0)).save(any(BidList.class));
  }

  @Test
  public void updateBadBodyTest() throws Exception {
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content("")
    ).andExpect(status().isBadRequest());
    verify(bidListServiceMocked,times(0)).update(any(BidList.class),any(BidList.class));
  }

  @Test
  public void updateWithNotFoundBidListTest() throws Exception {
    when(bidListServiceMocked.findById(28)).thenReturn(null);
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonOfGivenBidList)
    ).andExpect(status().isNotFound());
    verify(bidListServiceMocked,times(0)).update(any(BidList.class),any(BidList.class));
  }

  @Test
  public void updateTest() throws Exception {
    givenBidList.setBidListId(28);
    jsonOfGivenBidList = gson.toJson(givenBidList);
    when(bidListServiceMocked.findById(any(Integer.class))).thenReturn(Optional.of(givenBidList));
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonOfGivenBidList)
    ).andExpect(status().isOk());
    verify(bidListServiceMocked,times(1)).update(any(BidList.class),any(BidList.class));
  }

}
