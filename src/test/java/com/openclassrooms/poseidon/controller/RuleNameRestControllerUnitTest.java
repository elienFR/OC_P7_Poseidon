package com.openclassrooms.poseidon.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openclassrooms.poseidon.domain.RuleName;
import com.openclassrooms.poseidon.service.RuleNameService;
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
public class RuleNameRestControllerUnitTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RuleNameService ruleNameServiceMocked;

  @Value("${api.ver}")
  private String apiVer;

  private String baseUrl;

  private RuleName givenRuleName;

  private Gson gson
    = new GsonBuilder()
    .setPrettyPrinting()
    .create();

  private String jsonOfGivenRuleName;

  @BeforeEach
  public void setUp() {
    //URL base setup
    baseUrl = "/" + apiVer + "/ruleName";

    String givenName = "someName";
    String givenDescription = "someDescription";
    String givenJson = "someJson";
    String givenTemplate = "someTemplate";
    String givenSqlStr = "someSqlStr";
    String givenSqlPart = "someSqlPart";
    givenRuleName = new RuleName();
    givenRuleName.setName(givenName);
    givenRuleName.setDescription(givenDescription);
    givenRuleName.setJson(givenJson);
    givenRuleName.setTemplate(givenTemplate);
    givenRuleName.setSqlStr(givenSqlStr);
    givenRuleName.setSqlPart(givenSqlPart);

    jsonOfGivenRuleName = gson.toJson(givenRuleName);
  }

  @WithMockUser(username = "sringuser")
  @Test
  public void getAllUnauthorizedTest() throws Exception {
    mockMvc.perform(get(baseUrl + "/list"))
      .andExpect(status().isForbidden());
    verify(ruleNameServiceMocked, times(0)).getAll();
  }

  @Test
  public void getAllTest() throws Exception {
    mockMvc.perform(get(baseUrl + "/list"))
      .andExpect(status().isOk());
    verify(ruleNameServiceMocked, times(1)).getAll();
  }

  @Test
  public void getByIdTest() throws Exception {
    Integer givenInteger = 1;
    mockMvc.perform(get(baseUrl + "/list/" + givenInteger))
      .andExpect(status().isOk());
    verify(ruleNameServiceMocked, times(1)).findById(1);
  }

  @Test
  public void deleteTest() throws Exception {
    when(ruleNameServiceMocked.findById(1)).thenReturn(Optional.of(new RuleName()));
    mockMvc.perform(
        delete(baseUrl + "/delete").param("id","1")
      )
      .andExpect(status().isOk());
    verify(ruleNameServiceMocked, times(1)).findById(1);
  }

  @Test
  public void deleteTestWithNotFound() throws Exception {
    when(ruleNameServiceMocked.findById(1)).thenReturn(null);
    mockMvc.perform(
        delete(baseUrl + "/delete").param("id","1")
      )
      .andExpect(status().isNotFound());
    verify(ruleNameServiceMocked, times(0)).delete(any(RuleName.class));
  }

  @Test
  public void createTest() throws Exception {
    when(ruleNameServiceMocked.save(any(RuleName.class))).thenReturn(givenRuleName);
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonOfGivenRuleName)
      )
      .andExpect(status().isOk());
    verify(ruleNameServiceMocked,times(1)).save(any(RuleName.class));
  }

  @Test
  public void createBadBodyTest() throws Exception {
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content("")
      )
      .andExpect(status().isBadRequest());
    verify(ruleNameServiceMocked,times(0)).save(any(RuleName.class));
  }

  @Test
  public void createNotValidTest() throws Exception {
    givenRuleName.setName(null);
    jsonOfGivenRuleName = gson.toJson(givenRuleName);
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonOfGivenRuleName)
      )
      .andExpect(status().isBadRequest());
    verify(ruleNameServiceMocked,times(0)).save(any(RuleName.class));
  }

  @Test
  public void updateBadBodyTest() throws Exception {
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content("")
    ).andExpect(status().isBadRequest());
    verify(ruleNameServiceMocked,times(0)).update(any(RuleName.class),any(RuleName.class));
  }

  @Test
  public void updateWithNotFoundBidListTest() throws Exception {
    when(ruleNameServiceMocked.findById(28)).thenReturn(null);
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonOfGivenRuleName)
    ).andExpect(status().isNotFound());
    verify(ruleNameServiceMocked,times(0)).update(any(RuleName.class),any(RuleName.class));
  }

  @Test
  public void updateTest() throws Exception {
    givenRuleName.setId(28);
    jsonOfGivenRuleName = gson.toJson(givenRuleName);
    when(ruleNameServiceMocked.findById(any(Integer.class))).thenReturn(Optional.of(givenRuleName));
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonOfGivenRuleName)
    ).andExpect(status().isOk());
    verify(ruleNameServiceMocked,times(1)).update(any(RuleName.class),any(RuleName.class));
  }

}
