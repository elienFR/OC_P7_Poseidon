package com.openclassrooms.poseidon.service;

import com.openclassrooms.poseidon.domain.Authority;
import com.openclassrooms.poseidon.domain.DTO.UserDTO;
import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.domain.utils.Role;
import com.openclassrooms.poseidon.repository.UserRepository;
import com.openclassrooms.poseidon.service.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceUnitTest {

  @Autowired
  private UserService userServiceUnderTest;

  @MockBean
  private UserRepository userRepositoryMocked;


  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B);

  private User givenUser;
  private String givenUsername;
  private String givenFullName;
  private String givenRawPassword;
  private Authority givenAuthority;
  private UserDTO givenUserDTO;

  @BeforeEach
  public void setUp() {
    givenUsername = "someUsername";
    givenFullName = "someFullName";
    givenRawPassword = "somePassword";
    givenAuthority = new Authority();
    givenAuthority.setRole(Role.ROLE_USER);

    givenUser = new User();
    givenUser.setUsername(givenUsername);
    givenUser.setFullname(givenFullName);
    givenUser.setPassword(givenRawPassword);
    givenUser.setEnabled(true);
    givenUser.addAuthority(givenAuthority);

    givenUserDTO = new UserDTO();
    givenUserDTO.setUsername(givenUser.getUsername());
    givenUserDTO.setFullname(givenUser.getFullname());
    givenUserDTO.setEnabled(givenUser.isEnabled());
    givenUserDTO.setRole("ROLE_USER");
    givenUserDTO.setPassword(givenRawPassword);
  }

  @Test
  public void getAllTest() {
    Iterable<User> expected = new ArrayList<>();
    when(userRepositoryMocked.findAll()).thenReturn(expected);

    Iterable<User> result = userServiceUnderTest.getAll();

    assertThat(result).isEqualTo(expected);
    verify(userRepositoryMocked, times(1)).findAll();
  }

  @Test
  public void getAllDTOTest() {
    List<User> expected = new ArrayList<>();
    User user1 = new User();
    User user2 = new User();
    User user3 = new User();
    expected.addAll(List.of(user1, user2, user3));

    when(userRepositoryMocked.findAll()).thenReturn(expected);

    List<UserDTO> results = Lists.newArrayList(userServiceUnderTest.getAllDTO());
    assertThat(results.size()).isEqualTo(3);
    for (UserDTO result : results) {
      assertThat(result).isInstanceOf(UserDTO.class);
    }
  }

  @Test
  public void saveTest() {
    User expectedUser = new User();
    expectedUser.setUsername(givenUser.getUsername());
    expectedUser.setFullname(givenUser.getFullname());
    expectedUser.setEnabled(givenUser.isEnabled());
    expectedUser.addAuthority(givenUser.getAuthorities().get(0));
    expectedUser.setId(28);
    String encodedPassword = passwordEncoder.encode(givenRawPassword);
    expectedUser.setPassword(encodedPassword);

    when(userRepositoryMocked.save(givenUser)).thenReturn(expectedUser);

    User result = userServiceUnderTest.save(givenUser);

    assertThat(result.getId()).isEqualTo(28);
    assertThat(
      passwordEncoder.matches(givenRawPassword, result.getPassword())).isTrue();
  }

  @Test
  public void saveDTOTest() {
    User expectedUser = new User();
    expectedUser.setUsername(givenUser.getUsername());
    expectedUser.setFullname(givenUser.getFullname());
    expectedUser.setEnabled(givenUser.isEnabled());
    expectedUser.addAuthority(givenUser.getAuthorities().get(0));
    expectedUser.setId(28);
    String encodedPassword = passwordEncoder.encode(givenRawPassword);
    expectedUser.setPassword(encodedPassword);

    when(userRepositoryMocked.save(any(User.class))).thenReturn(expectedUser);

    UserDTO result = userServiceUnderTest.saveDTO(givenUserDTO);

    assertThat(result.getId()).isEqualTo(28);
    assertThat(passwordEncoder.matches(givenRawPassword, result.getPassword())).isTrue();
    assertThat(result.getRole()).isEqualTo("ROLE_USER");
  }

  @Test
  public void saveAdminDTOTest() {
    Authority givenAuthorityAdmin = new Authority();
    givenAuthorityAdmin.setRole(Role.ROLE_ADMIN);
    givenUser.addAuthority(givenAuthorityAdmin);

    User expectedUser = new User();
    expectedUser.setUsername(givenUser.getUsername());
    expectedUser.setFullname(givenUser.getFullname());
    expectedUser.setEnabled(givenUser.isEnabled());
    expectedUser.addAuthority(givenUser.getAuthorities().get(0));
    expectedUser.addAuthority(givenAuthorityAdmin);
    expectedUser.setId(28);
    String encodedPassword = passwordEncoder.encode(givenRawPassword);
    expectedUser.setPassword(encodedPassword);

    when(userRepositoryMocked.save(any(User.class))).thenReturn(expectedUser);

    UserDTO result = userServiceUnderTest.saveDTO(givenUserDTO);

    assertThat(result.getId()).isEqualTo(28);
    assertThat(passwordEncoder.matches(givenRawPassword, result.getPassword())).isTrue();
    assertThat(result.getRole()).isEqualTo("ROLE_ADMIN");
  }

  @Test
  public void findByIdTest() {
    User user = new User();
    Optional<User> expected = Optional.of(user);

    when(userRepositoryMocked.findById(4)).thenReturn(expected);

    Optional<User> result = userServiceUnderTest.findById(4);

    assertThat(result).isEqualTo(expected);
    verify(userRepositoryMocked, times(1)).findById(4);
  }

  @Test
  public void updateIdMismatchTest() {
    User givenModifiedUser = new User();
    givenModifiedUser.setId(28);

    String expectedMessage = "Users ID mismatch.";

    Exception exception = assertThrows(RuntimeException.class, () -> {
      userServiceUnderTest.update(givenUser, givenModifiedUser);
    });

    String resultMessage = exception.getMessage();

    assertTrue(resultMessage.contains(expectedMessage));
  }

  @Test
  public void updateTestWithNoAuthoritiesChange() {
    String someNewFullName = "someNewFullName";
    String someNewUserName = "someNewUserName";
    String someNewRawPassword = "someNewRawPassword";
    //So that the update works, we need to set up an id first
    givenUser.setId(28);
    User givenModifiedUser = new User();
    givenModifiedUser.setUsername(someNewUserName);
    givenModifiedUser.setFullname(someNewFullName);
    givenModifiedUser.setEnabled(givenUser.isEnabled());
    givenModifiedUser.addAuthority(givenUser.getAuthorities().get(0));
    givenModifiedUser.setId(28);
    givenModifiedUser.setPassword(someNewRawPassword);

    when(userRepositoryMocked.save(givenUser)).thenReturn(givenUser);

    User result = userServiceUnderTest.update(givenModifiedUser, givenUser);

    verify(userRepositoryMocked, times(1)).save(givenUser);
    assertThat(result.getUsername()).isEqualTo(someNewUserName);
    assertThat(result.getFullname()).isEqualTo(someNewFullName);
    assertThat(passwordEncoder.matches(someNewRawPassword,result.getPassword()));
    assertThat(result.getAuthorities()).isEqualTo(givenUser.getAuthorities());
  }

  @Test
  public void updateDTOTest() {
    String someNewFullName = "someNewFullName";
    String someNewUserName = "someNewUserName";
    String someNewRawPassword = "someNewRawPassword";
    //So that the update works, we need to set up an id first
    givenUser.setId(28);
    UserDTO givenModifiedUserDTO = new UserDTO();
    givenModifiedUserDTO.setUsername(someNewUserName);
    givenModifiedUserDTO.setFullname(someNewFullName);
    givenModifiedUserDTO.setEnabled(givenUser.isEnabled());
    givenModifiedUserDTO.setRole("ROLE_USER");
    givenModifiedUserDTO.setId(28);
    givenModifiedUserDTO.setPassword(someNewRawPassword);

    when(userRepositoryMocked.save(givenUser)).thenReturn(givenUser);
    when(userRepositoryMocked.findById(28)).thenReturn(Optional.of(givenUser));

    UserDTO result = userServiceUnderTest.updateDTO(givenModifiedUserDTO, givenUser);

    verify(userRepositoryMocked, times(1)).save(givenUser);
    assertThat(result.getUsername()).isEqualTo(someNewUserName);
    assertThat(result.getFullname()).isEqualTo(someNewFullName);
    assertThat(passwordEncoder.matches(someNewRawPassword,result.getPassword()));
    assertThat(result.getRole()).isEqualTo(givenUser.getAuthorities().get(0).getRole().toString());
  }

  @Test
  public void deleteTest() {
    User userToDelete = new User();

    userServiceUnderTest.delete(userToDelete);

    verify(userRepositoryMocked, times(1)).delete(userToDelete);
  }

  @Test
  public void findDTOByIdTest() {
    Optional<UserDTO> result = userServiceUnderTest.findDTOById(28);

    verify(userRepositoryMocked, times(1)).findById(28);
  }

}
