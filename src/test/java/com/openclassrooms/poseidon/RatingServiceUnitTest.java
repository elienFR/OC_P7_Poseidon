package com.openclassrooms.poseidon;

import com.openclassrooms.poseidon.domain.Rating;
import com.openclassrooms.poseidon.repository.RatingRepository;
import com.openclassrooms.poseidon.service.RatingService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RatingServiceUnitTest {

  @Autowired
  private RatingService ratingServiceUnderTest;

  @MockBean
  private RatingRepository ratingRepositoryMocked;

  private Rating givenRating;

  @BeforeEach
  public void setUp() {
    String givenMoodyRating = "someMoodysRating";
    String givenSandPRating = "someSandPRating";
    String givenFitchRating = "someFitchRating";
    Integer givenOrderNumber = 1;
    givenRating = new Rating();
    givenRating.setMoodysRating(givenMoodyRating);
    givenRating.setSandPRating(givenSandPRating);
    givenRating.setFitchRating(givenFitchRating);
    givenRating.setOrderNumber(givenOrderNumber);
  }

  @Test
  public void getAllTest() {
    List<Rating> expected = new ArrayList<>();

    when(ratingRepositoryMocked.findAll()).thenReturn(expected);

    List<Rating> result = Lists.newArrayList(ratingServiceUnderTest.getAll());

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void saveExceptionTest() {
    givenRating.setId(28);
    String expectedMessage = "Forbidden to save a rating with specific id";

    Exception exception = assertThrows(
      ResponseStatusException.class,
      () -> ratingServiceUnderTest.save(givenRating)
    );
    String resultMessage = exception.getMessage();

    assertThat(resultMessage.contains(expectedMessage)).isTrue();
  }

  @Test
  public void saveTest() {
    when(ratingRepositoryMocked.save(givenRating)).thenReturn(givenRating);

    Rating result = ratingServiceUnderTest.save(givenRating);

    assertThat(result).isEqualTo(givenRating);
  }

  @Test
  public void findByIdTest() {
    Integer givenId = 28;

    when(ratingRepositoryMocked.findById(givenId)).thenReturn(Optional.of(givenRating));

    Optional<Rating> result = ratingServiceUnderTest.findById(givenId);

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get()).isEqualTo(givenRating);
  }

  @Test
  public void updateExceptionTest() {
    Rating modifiedRating = new Rating();
    modifiedRating.setId(28);

    String expectedMessage = "Rating ID mismatch.";

    Exception exception = assertThrows(
      RuntimeException.class,
      () -> ratingServiceUnderTest.update(modifiedRating, givenRating)
    );
    String resultMessage = exception.getMessage();

    assertThat(resultMessage.contains(expectedMessage)).isTrue();
  }

  @Test
  public void updateTest() {
    Integer givenId = 28;
    String newGivenMoodyRating = "newMoodysRating";
    String newGivenSandPRating = "newSandPRating";
    String newGivenFitchRating = "newFitchRating";
    Integer newGivenOrderNumber = 42;

    givenRating.setId(givenId);
    Rating modifiedRating = new Rating();
    modifiedRating.setId(givenId);
    modifiedRating.setFitchRating(newGivenFitchRating);
    modifiedRating.setMoodysRating(newGivenMoodyRating);
    modifiedRating.setSandPRating(newGivenSandPRating);
    modifiedRating.setOrderNumber(newGivenOrderNumber);

    when(ratingRepositoryMocked.save(givenRating)).thenReturn(givenRating);

    Rating result = ratingServiceUnderTest.update(modifiedRating, givenRating);

    assertThat(result.getFitchRating()).isEqualTo(newGivenFitchRating);
    assertThat(result.getMoodysRating()).isEqualTo(newGivenMoodyRating);
    assertThat(result.getSandPRating()).isEqualTo(newGivenSandPRating);
    assertThat(result.getOrderNumber()).isEqualTo(newGivenOrderNumber);
  }

  @Test
  public void deleteTest() {
    ratingServiceUnderTest.delete(givenRating);

    verify(ratingRepositoryMocked, times(1)).delete(givenRating);
  }

}
