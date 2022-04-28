package com.openclassrooms.poseidon.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openclassrooms.poseidon.domain.DTO.UserDTO;
import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.service.UserService;
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
public class UserRestControllerUnitTest {


  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userServiceMocked;

  @Value("${api.ver}")
  private String apiVer;

  private String baseUrl;

  private UserDTO givenUserDTO;

  private User givenUser;

  private Gson gson
    = new GsonBuilder()
    .setPrettyPrinting()
    .create();

  private String jsonOfGivenUser;

  @BeforeEach
  public void setUp() {
    //URL base setup
    baseUrl = "/" + apiVer + "/user";

    String givenUsername = "someUsername";
    String givenFullName = "someFullName";
    String givenRawPassword = "somePassword";
    givenUser = new User();
    givenUser.setUsername(givenUsername);
    givenUser.setFullname(givenFullName);
    givenUser.setPassword(givenRawPassword);
    givenUser.setEnabled(true);

    givenUserDTO = new UserDTO();
    givenUserDTO.setUsername(givenUsername);
    givenUserDTO.setFullname(givenFullName);
    givenUserDTO.setEnabled(true);
    givenUserDTO.setRole("ROLE_USER");
    givenUserDTO.setPassword(givenRawPassword);

    jsonOfGivenUser = gson.toJson(givenUserDTO);
  }

  @WithMockUser(username = "sringuser")
  @Test
  public void getAllUnauthorizedTest() throws Exception {
    mockMvc.perform(get(baseUrl + "/list"))
      .andExpect(status().isForbidden());
    verify(userServiceMocked, times(0)).getAll();
  }

  @Test
  public void getAllTest() throws Exception {
    mockMvc.perform(get(baseUrl + "/list"))
      .andExpect(status().isOk());
    verify(userServiceMocked, times(1)).getAllDTO();
  }

  @Test
  public void getByIdTest() throws Exception {
    Integer givenInteger = 1;
    mockMvc.perform(get(baseUrl + "/list/" + givenInteger))
      .andExpect(status().isOk());
    verify(userServiceMocked, times(1)).findDTOById(1);
  }

  @Test
  public void deleteTest() throws Exception {
    when(userServiceMocked.findById(1)).thenReturn(Optional.of(new User()));
    mockMvc.perform(
        delete(baseUrl + "/delete").param("id", "1")
      )
      .andExpect(status().isOk());
    verify(userServiceMocked, times(1)).findById(1);
  }

  @Test
  public void deleteTestWithNotFound() throws Exception {
    when(userServiceMocked.findById(1)).thenReturn(null);
    mockMvc.perform(
        delete(baseUrl + "/delete").param("id", "1")
      )
      .andExpect(status().isNotFound());
    verify(userServiceMocked, times(0)).delete(any(User.class));
  }

  @Test
  public void createTest() throws Exception {
    when(userServiceMocked.saveDTO(any(UserDTO.class))).thenReturn(givenUserDTO);
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonOfGivenUser)
      )
      .andExpect(status().isOk());
    verify(userServiceMocked, times(1)).saveDTO(any(UserDTO.class));
  }

  @Test
  public void createBadBodyTest() throws Exception {
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content("")
      )
      .andExpect(status().isBadRequest());
    verify(userServiceMocked, times(0)).saveDTO(any(UserDTO.class));
  }

  @Test
  public void createNotValidTest() throws Exception {
    givenUserDTO.setFullname(null);
    jsonOfGivenUser = gson.toJson(givenUserDTO);
    mockMvc.perform(
        post(baseUrl + "/add")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonOfGivenUser)
      )
      .andExpect(status().isBadRequest());
    verify(userServiceMocked, times(0)).saveDTO(any(UserDTO.class));
  }

  @Test
  public void updateBadBodyTest() throws Exception {
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content("null")
    ).andExpect(status().isBadRequest());
    verify(userServiceMocked, times(0)).updateDTO(any(UserDTO.class), any(User.class));
  }

  @Test
  public void updateWithNotFoundBidListTest() throws Exception {
    when(userServiceMocked.findById(28)).thenReturn(null);
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonOfGivenUser)
    ).andExpect(status().isNotFound());
    verify(userServiceMocked, times(0)).updateDTO(any(UserDTO.class), any(User.class));
  }

  @Test
  public void updateTest() throws Exception {
    givenUserDTO.setId(28);
    jsonOfGivenUser = gson.toJson(givenUserDTO);
    Optional<User> optionalUser = Optional.of(givenUser);
    when(userServiceMocked.findById(any(Integer.class))).thenReturn(optionalUser);
    mockMvc.perform(
      put(baseUrl + "/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonOfGivenUser)
    ).andExpect(status().isOk());
    verify(userServiceMocked, times(1)).updateDTO(any(UserDTO.class), any(User.class));
  }

}
