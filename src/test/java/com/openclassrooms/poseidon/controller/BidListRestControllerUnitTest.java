package com.openclassrooms.poseidon.controller;

import com.openclassrooms.poseidon.service.BidListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "springadmin", roles = {"ADMIN", "USER"})
public class BidListRestControllerUnitTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BidListService bidListServiceMocked;

  @Value("${api.ver}")
  private String apiVer;

  private String baseUrl;

  @BeforeEach
  public void init() {
    baseUrl = "/" + apiVer + "/bidList";
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
      .andExpect(status().isOk())
      .andExpect(content().json("{name: \"jason\"}"));
  }

}
