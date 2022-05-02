package com.openclassrooms.poseidon.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openclassrooms.poseidon.domain.CurvePoint;
import com.openclassrooms.poseidon.service.CurvePointService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql({"/test-schema.sql", "/test-data.sql"})
@WithMockUser(username = "springadmin", roles = {"ADMIN", "USER"})
public class CurvePointRestControllerUnitTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CurvePointService curvePointServiceMocked;

  @Value("${api.ver}")
  private String apiVer;

  private String baseUrl;

  private CurvePoint givenCurvePoint;

  private Gson gson
    = new GsonBuilder()
    .setPrettyPrinting()
    .create();

  private String jsonOfGivenCurvePoint;

  @BeforeEach
  public void setUp() {
    //URL base setup
    baseUrl = "/" + apiVer + "/curvePoint";

    givenCurvePoint = new CurvePoint();
    Integer givenCurveId = 1;
    Double givenTerm = 2.0d;
    Double givenValue = 3.0d;
    givenCurvePoint = new CurvePoint();
    givenCurvePoint.setCurveId(givenCurveId);
    givenCurvePoint.setTerm(givenTerm);
    givenCurvePoint.setValue(givenValue);

    jsonOfGivenCurvePoint = gson.toJson(givenCurvePoint);
  }

  @WithMockUser(username = "sringuser")
  @Test
  public void getAllUnauthorizedTest() throws Exception {
    mockMvc.perform(get(baseUrl + "/list"))
      .andExpect(status().isForbidden());
    verify(curvePointServiceMocked, times(0)).getAll();
  }

  @Test
  public void getAllTest() throws Exception {
    mockMvc.perform(get(baseUrl + "/list"))
      .andExpect(status().isOk());
    verify(curvePointServiceMocked, times(1)).getAll();
  }

  @Test
  public void getByIdTest() throws Exception {
    Integer givenInteger = 1;
    mockMvc.perform(get(baseUrl + "/list/" + givenInteger))
      .andExpect(status().isOk());
    verify(curvePointServiceMocked, times(1)).findById(1);
  }

  @Test
  public void deleteTest() throws Exception {
    when(curvePointServiceMocked.findById(1)).thenReturn(Optional.of(new CurvePoint()));
    mockMvc.perform(
        delete(baseUrl + "/delete").param("id","1")
      )
      .andExpect(status().isOk());
    verify(curvePointServiceMocked, times(1)).findById(1);
  }

  @Test
  public void deleteTestWithNotFound() throws Exception {
    when(curvePointServiceMocked.findById(1)).thenReturn(null);
    mockMvc.perform(
        delete(baseUrl + "/delete").param("id","1")
      )
      .andExpect(status().isNotFound());
    verify(curvePointServiceMocked, times(0)).delete(any(CurvePoint.class));
  }

  @Test
  public void createTest() throws Exception {
    when(curvePointServiceMocked.save(any(CurvePoint.class))).thenReturn(givenCurvePoint);
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonOfGivenCurvePoint)
      )
      .andExpect(status().isOk());
    verify(curvePointServiceMocked,times(1)).save(any(CurvePoint.class));
  }

  @Test
  public void createBadBodyTest() throws Exception {
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content("")
      )
      .andExpect(status().isBadRequest());
    verify(curvePointServiceMocked,times(0)).save(any(CurvePoint.class));
  }

  @Test
  public void createNotValidTest() throws Exception {
    givenCurvePoint.setTerm(null);
    jsonOfGivenCurvePoint = gson.toJson(givenCurvePoint);
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonOfGivenCurvePoint)
      )
      .andExpect(status().isBadRequest());
    verify(curvePointServiceMocked,times(0)).save(any(CurvePoint.class));
  }

  @Test
  public void updateBadBodyTest() throws Exception {
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content("")
    ).andExpect(status().isBadRequest());
    verify(curvePointServiceMocked,times(0)).update(any(CurvePoint.class),any(CurvePoint.class));
  }

  @Test
  public void updateWithNotFoundBidListTest() throws Exception {
    when(curvePointServiceMocked.findById(28)).thenReturn(null);
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonOfGivenCurvePoint)
    ).andExpect(status().isNotFound());
    verify(curvePointServiceMocked,times(0)).update(any(CurvePoint.class),any(CurvePoint.class));
  }

  @Test
  public void updateTest() throws Exception {
    givenCurvePoint.setId(28);
    jsonOfGivenCurvePoint = gson.toJson(givenCurvePoint);
    when(curvePointServiceMocked.findById(any(Integer.class))).thenReturn(Optional.of(givenCurvePoint));
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonOfGivenCurvePoint)
    ).andExpect(status().isOk());
    verify(curvePointServiceMocked,times(1)).update(any(CurvePoint.class),any(CurvePoint.class));
  }

}
