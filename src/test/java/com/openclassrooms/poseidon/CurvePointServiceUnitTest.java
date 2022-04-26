package com.openclassrooms.poseidon;

import com.nimbusds.jose.jwk.Curve;
import com.openclassrooms.poseidon.domain.CurvePoint;
import com.openclassrooms.poseidon.repository.CurvePointRepository;
import com.openclassrooms.poseidon.service.CurvePointService;
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
public class CurvePointServiceUnitTest {

  @Autowired
  private CurvePointService curvePointServiceUnderTest;

  @MockBean
  private CurvePointRepository curvePointRepositoryMocked;

  private CurvePoint givenCurvePoint;

  @BeforeEach
  public void setUp() {
    Integer givenCurveId = 1;
    Double givenTerm = 2.0d;
    Double givenValue = 3.0d;
    givenCurvePoint = new CurvePoint();
    givenCurvePoint.setCurveId(givenCurveId);
    givenCurvePoint.setTerm(givenTerm);
    givenCurvePoint.setValue(givenValue);
  }

  @Test
  public void getAllTest() {
    Iterable<CurvePoint> expected = new ArrayList<>();

    when(curvePointRepositoryMocked.findAll()).thenReturn(expected);

    Iterable<CurvePoint> result = curvePointServiceUnderTest.getAll();

    assertThat(result).isEqualTo(expected);
    verify(curvePointRepositoryMocked, times(1)).findAll();
  }

  @Test
  public void findByIdTest() {
    Integer givenId = 28;
    givenCurvePoint.setId(givenId);

    when(curvePointRepositoryMocked.findById(givenId)).thenReturn(Optional.of(givenCurvePoint));

    Optional<CurvePoint> result = curvePointServiceUnderTest.findById(givenId);

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getId()).isEqualTo(28);
    verify(curvePointRepositoryMocked, times(1)).findById(givenId);
  }

  @Test
  public void deleteTest() {
    curvePointServiceUnderTest.delete(givenCurvePoint);
    verify(curvePointRepositoryMocked, times(1)).delete(givenCurvePoint);
  }

  @Test
  public void saveWithExceptionTest() {
    givenCurvePoint.setId(28);
    String expectedMessage = "Forbidden to save a curve point with specific id";

    Exception exception = assertThrows(ResponseStatusException.class,
      () -> curvePointServiceUnderTest.save(givenCurvePoint)
    );

    String resultMessage = exception.getMessage();
    assertThat(resultMessage.contains(expectedMessage)).isTrue();
  }

  @Test
  public void saveTest() {
    when(curvePointRepositoryMocked.save(givenCurvePoint)).thenReturn(givenCurvePoint);

    CurvePoint result = curvePointServiceUnderTest.save(givenCurvePoint);

    assertThat(result).isEqualTo(givenCurvePoint);
    assertThat(result.getCreationDate()).isNotNull();
  }

  @Test
  public void updateWithExceptionTest() {
    CurvePoint modifiedCurvePoint = new CurvePoint();
    modifiedCurvePoint.setId(42);

    String expectedMessage = "Curve point ID mismatch.";

    Exception exception = assertThrows(
      RuntimeException.class,
      () -> curvePointServiceUnderTest.update(modifiedCurvePoint, givenCurvePoint)
    );

    String resultMessage = exception.getMessage();

    assertThat(resultMessage.contains(expectedMessage)).isTrue();
  }

  @Test
  public void updateTest() {
    givenCurvePoint.setId(42);
    Integer newGivenCurveId = 28;
    Double newGivenTerm = 9.0d;
    Double newGivenValue = 100.0d;
    CurvePoint modifiedCurvePoint = new CurvePoint();
    modifiedCurvePoint.setId(42);
    modifiedCurvePoint.setCurveId(newGivenCurveId);
    modifiedCurvePoint.setTerm(newGivenTerm);
    modifiedCurvePoint.setValue(newGivenValue);

    when(curvePointRepositoryMocked.save(givenCurvePoint)).thenReturn(givenCurvePoint);

    CurvePoint result = curvePointServiceUnderTest.update(modifiedCurvePoint, givenCurvePoint);

    verify(curvePointRepositoryMocked, times(1)).save(givenCurvePoint);
    assertThat(result.getCurveId()).isEqualTo(28);
    assertThat(result.getTerm()).isEqualTo(9.0d);
    assertThat(result.getValue()).isEqualTo(100.0d);
  }

}
