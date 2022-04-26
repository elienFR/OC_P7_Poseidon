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

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    verify(curvePointRepositoryMocked,times(1)).findById(givenId);
  }

  @Test
  public void deleteTest() {

  }

}
