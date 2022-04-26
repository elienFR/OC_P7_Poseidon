package com.openclassrooms.poseidon;

import com.openclassrooms.poseidon.domain.RuleName;
import com.openclassrooms.poseidon.repository.RuleNameRepository;
import com.openclassrooms.poseidon.service.RuleNameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RuleNameServiceUnitTest {

  @Autowired
  private RuleNameService ruleNameServiceUnderTest;

  @MockBean
  private RuleNameRepository ruleNameRepositoryMocked;

  private RuleName givenRuleName;

  @BeforeEach
  public void setUp() {
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
  }

  @Test
  public void getAllTest() {
    Iterable<RuleName> expected = new ArrayList<>();

    when(ruleNameRepositoryMocked.findAll()).thenReturn(expected);

    Iterable<RuleName> result = ruleNameServiceUnderTest.getAll();

    assertThat(result).isEqualTo(expected);
    verify(ruleNameRepositoryMocked, times(1)).findAll();
  }

  @Test
  public void saveExceptionTest() {
    givenRuleName.setId(28);
    String expectedMessage = "Forbidden to save a rule name with specific id";

    Exception exception = assertThrows(
      ResponseStatusException.class,
      () -> ruleNameServiceUnderTest.save(givenRuleName)
    );

    String resultMessage = exception.getMessage();

    assertThat(resultMessage.contains(expectedMessage)).isTrue();
  }

  @Test
  public void saveTest() {
    when(ruleNameRepositoryMocked.save(givenRuleName)).thenReturn(givenRuleName);

    RuleName result = ruleNameServiceUnderTest.save(givenRuleName);

    assertThat(result).isEqualTo(givenRuleName);
    verify(ruleNameRepositoryMocked, times(1)).save(givenRuleName);
  }

  @Test
  public void findByIdTest() {
    Integer givenId = 28;
    givenRuleName.setId(givenId);

    when(ruleNameRepositoryMocked.findById(givenId)).thenReturn(Optional.of(givenRuleName));

    Optional<RuleName> result = ruleNameServiceUnderTest.findById(givenId);

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get()).isEqualTo(givenRuleName);
    verify(ruleNameRepositoryMocked, times(1)).findById(givenId);
  }

  @Test
  public void updateExceptionTest() {
    RuleName modifiedRuleName = new RuleName();
    modifiedRuleName.setId(28);
    String expectedMessage = "Rule names ID mismatch.";

    Exception exception = assertThrows(
      RuntimeException.class,
      () -> ruleNameServiceUnderTest.update(modifiedRuleName, givenRuleName)
    );

    String resultMessage = exception.getMessage();

    assertThat(resultMessage.contains(expectedMessage)).isTrue();
  }

  @Test
  public void updateTest() {
    Integer givenId = 12;
    givenRuleName.setId(givenId);
    String newGivenName = "newName";
    String newGivenDescription = "newDescription";
    String newGivenJson = "newJson";
    String newGivenTemplate = "newTemplate";
    String newGivenSqlStr = "newSqlStr";
    String newGivenSqlPart = "newSqlPart";
    RuleName modifiedRuleName = new RuleName();
    modifiedRuleName.setId(givenId);
    modifiedRuleName.setName(newGivenName);
    modifiedRuleName.setDescription(newGivenDescription);
    modifiedRuleName.setJson(newGivenJson);
    modifiedRuleName.setTemplate(newGivenTemplate);
    modifiedRuleName.setSqlStr(newGivenSqlStr);
    modifiedRuleName.setSqlPart(newGivenSqlPart);
    
    when(ruleNameRepositoryMocked.save(givenRuleName)).thenReturn(givenRuleName);
    
    RuleName result = ruleNameServiceUnderTest.update(modifiedRuleName, givenRuleName);

    verify(ruleNameRepositoryMocked, times(1)).save(givenRuleName);
    assertThat(result.getName()).isEqualTo(newGivenName);
    assertThat(result.getDescription()).isEqualTo(newGivenDescription);
    assertThat(result.getJson()).isEqualTo(newGivenJson);
    assertThat(result.getTemplate()).isEqualTo(newGivenTemplate);
    assertThat(result.getSqlStr()).isEqualTo(newGivenSqlStr);
    assertThat(result.getSqlPart()).isEqualTo(newGivenSqlPart);
  }

  @Test
  public void deleteTest() {
    ruleNameServiceUnderTest.delete(givenRuleName);

    verify(ruleNameRepositoryMocked, times(1)).delete(givenRuleName);
  }

}
