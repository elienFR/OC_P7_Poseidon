package com.openclassrooms.poseidon.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openclassrooms.poseidon.domain.Rating;
import com.openclassrooms.poseidon.service.RatingService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "springadmin", roles = {"ADMIN", "USER"})
public class RatingRestControllerUnitTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RatingService ratingServiceMocked;

  @Value("${api.ver}")
  private String apiVer;

  private String baseUrl;

  private Rating givenRating;

  private Gson gson
    = new GsonBuilder()
    .setPrettyPrinting()
    .create();

  private String jsonOfGivenRating;

  @BeforeEach
  public void setUp() {
    //URL base setup
    baseUrl = "/" + apiVer + "/rating";

    String givenMoodyRating = "someMoodysRating";
    String givenSandPRating = "someSandPRating";
    String givenFitchRating = "someFitchRating";
    Integer givenOrderNumber = 1;
    givenRating = new Rating();
    givenRating.setMoodysRating(givenMoodyRating);
    givenRating.setSandPRating(givenSandPRating);
    givenRating.setFitchRating(givenFitchRating);
    givenRating.setOrderNumber(givenOrderNumber);

    jsonOfGivenRating = gson.toJson(givenRating);
  }

  @WithMockUser(username = "sringuser")
  @Test
  public void getAllUnauthorizedTest() throws Exception {
    mockMvc.perform(get(baseUrl + "/list"))
      .andExpect(status().isForbidden());
    verify(ratingServiceMocked, times(0)).getAll();
  }

  @Test
  public void getAllTest() throws Exception {
    mockMvc.perform(get(baseUrl + "/list"))
      .andExpect(status().isOk());
    verify(ratingServiceMocked, times(1)).getAll();
  }

  @Test
  public void getByIdTest() throws Exception {
    Integer givenInteger = 1;
    mockMvc.perform(get(baseUrl + "/list/" + givenInteger))
      .andExpect(status().isOk());
    verify(ratingServiceMocked, times(1)).findById(1);
  }

  @Test
  public void deleteTest() throws Exception {
    when(ratingServiceMocked.findById(1)).thenReturn(Optional.of(new Rating()));
    mockMvc.perform(
        delete(baseUrl + "/delete").param("id","1")
      )
      .andExpect(status().isOk());
    verify(ratingServiceMocked, times(1)).findById(1);
  }

  @Test
  public void deleteTestWithNotFound() throws Exception {
    when(ratingServiceMocked.findById(1)).thenReturn(null);
    mockMvc.perform(
        delete(baseUrl + "/delete").param("id","1")
      )
      .andExpect(status().isNotFound());
    verify(ratingServiceMocked, times(0)).delete(any(Rating.class));
  }

  @Test
  public void createTest() throws Exception {
    when(ratingServiceMocked.save(any(Rating.class))).thenReturn(givenRating);
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonOfGivenRating)
      )
      .andExpect(status().isOk());
    verify(ratingServiceMocked,times(1)).save(any(Rating.class));
  }

  @Test
  public void createBadBodyTest() throws Exception {
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content("")
      )
      .andExpect(status().isBadRequest());
    verify(ratingServiceMocked,times(0)).save(any(Rating.class));
  }

  @Test
  public void createNotValidTest() throws Exception {
    givenRating.setSandPRating(null);
    jsonOfGivenRating = gson.toJson(givenRating);
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonOfGivenRating)
      )
      .andExpect(status().isBadRequest());
    verify(ratingServiceMocked,times(0)).save(any(Rating.class));
  }

  @Test
  public void updateBadBodyTest() throws Exception {
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content("")
    ).andExpect(status().isBadRequest());
    verify(ratingServiceMocked,times(0)).update(any(Rating.class),any(Rating.class));
  }

  @Test
  public void updateWithNotFoundBidListTest() throws Exception {
    when(ratingServiceMocked.findById(28)).thenReturn(null);
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonOfGivenRating)
    ).andExpect(status().isNotFound());
    verify(ratingServiceMocked,times(0)).update(any(Rating.class),any(Rating.class));
  }

  @Test
  public void updateTest() throws Exception {
    givenRating.setId(28);
    jsonOfGivenRating = gson.toJson(givenRating);
    when(ratingServiceMocked.findById(any(Integer.class))).thenReturn(Optional.of(givenRating));
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonOfGivenRating)
    ).andExpect(status().isOk());
    verify(ratingServiceMocked,times(1)).update(any(Rating.class),any(Rating.class));
  }

}
