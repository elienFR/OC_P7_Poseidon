package com.openclassrooms.poseidon;

import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.repository.BidListRepository;
import com.openclassrooms.poseidon.service.BidListService;
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
public class BidListServiceUnitTest {

  @Autowired
  private BidListService bidListServiceUnderTest;

  @MockBean
  private BidListRepository bidListRepositoryMocked;

  private BidList givenBidList;

  @BeforeEach
  public void setUp() {
    String givenAccount = "someAccount";
    String givenType = "someType";
    Double givenBidQuantity = 1.0d;
    givenBidList = new BidList();
    givenBidList.setAccount(givenAccount);
    givenBidList.setType(givenType);
    givenBidList.setBidQuantity(givenBidQuantity);
  }

  @Test
  public void getAllTest() {
    List<BidList> expected = new ArrayList<>();
    expected.add(givenBidList);

    when(bidListRepositoryMocked.findAll()).thenReturn(expected);

    List<BidList> result = Lists.newArrayList(bidListServiceUnderTest.getAll());

    assertThat(result).isEqualTo(expected);
    verify(bidListRepositoryMocked, times(1)).findAll();
  }

  @Test
  public void deleteTest() {
    bidListServiceUnderTest.delete(givenBidList);

    verify(bidListRepositoryMocked, times(1)).delete(givenBidList);
  }

  @Test
  public void findByIdTest() {
    givenBidList.setBidListId(28);
    Integer givenId = 28;

    when(bidListRepositoryMocked.findById(givenId)).thenReturn(Optional.of(givenBidList));

    Optional<BidList> result = bidListServiceUnderTest.findById(givenId);

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getBidListId()).isEqualTo(28);
  }

  @Test
  public void saveWithExistingIdTest() {
    givenBidList.setBidListId(28);
    String expectedMessage = "Forbidden to save a bid with specific id";
    Exception exception = assertThrows(
      ResponseStatusException.class, () -> bidListServiceUnderTest.save(givenBidList)
    );

    String resultMessage = exception.getMessage();

    assertThat(resultMessage.contains(expectedMessage)).isTrue();
  }

  @Test
  public void saveTest() {
    when(bidListRepositoryMocked.save(givenBidList)).thenReturn(givenBidList);

    BidList result = bidListServiceUnderTest.save(givenBidList);

    assertThat(result).isEqualTo(givenBidList);
    assertThat(result.getCreationDate()).isNotNull();
  }

  @Test
  public void updateExceptionTest() {
    BidList modifiedBidList = new BidList();
    modifiedBidList.setBidListId(42);

    String expectedMessage = "Bid's ID mismatch.";

    Exception exception = assertThrows(
      RuntimeException.class,
      () ->bidListServiceUnderTest.update(modifiedBidList, givenBidList)
    );

    String resultMessage = exception.getMessage();

    assertThat(resultMessage.contains(expectedMessage)).isTrue();
  }

  @Test
  public void updateTest() {
    givenBidList.setBidListId(42);

    String givenAccountToUpdate = "someNewAccount";
    String givenTypeToUpdate = "someNewType";
    Double givenBidQuantityToUpdate = 28.0d;
    BidList modifiedBidList = new BidList();
    modifiedBidList.setBidListId(42);
    modifiedBidList.setAccount(givenAccountToUpdate);
    modifiedBidList.setType(givenTypeToUpdate);
    modifiedBidList.setBidQuantity(givenBidQuantityToUpdate);

    when(bidListRepositoryMocked.save(givenBidList)).thenReturn(givenBidList);

    BidList result = bidListServiceUnderTest.update(modifiedBidList, givenBidList);

    assertThat(result.getBidListId()).isEqualTo(givenBidList.getBidListId());
    assertThat(result.getAccount()).isEqualTo(givenAccountToUpdate);
    assertThat(result.getType()).isEqualTo(givenTypeToUpdate);
    assertThat(result.getBidQuantity()).isEqualTo(28.0d);
    verify(bidListRepositoryMocked,times(1)).save(givenBidList);
  }


}
