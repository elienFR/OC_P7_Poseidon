package com.openclassrooms.poseidon;

import com.openclassrooms.poseidon.domain.Trade;
import com.openclassrooms.poseidon.repository.TradeRepository;
import com.openclassrooms.poseidon.service.TradeService;
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
public class TradeServiceUnitTest {

  @Autowired
  private TradeService tradeServiceUnderTest;

  @MockBean
  private TradeRepository tradeRepositoryMocked;

  private Trade givenTrade;

  @BeforeEach
  public void setUp(){
    String givenAccount = "someAccount";
    String givenType = "someType";
    Double givenBuyQuantity = 1.0d;
    givenTrade = new Trade();
    givenTrade.setAccount(givenAccount);
    givenTrade.setType(givenType);
    givenTrade.setBuyQuantity(givenBuyQuantity);
  }

  @Test
  public void getAllTest() {
    Iterable<Trade> expected = new ArrayList<>();

    when(tradeRepositoryMocked.findAll()).thenReturn(expected);

    Iterable<Trade> result = tradeServiceUnderTest.getAll();

    verify(tradeRepositoryMocked,times(1)).findAll();
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void saveExceptionTest() {
    givenTrade.setTradeId(28);

    String expectedMessage = "Forbidden to save a trade with specific id";
    Exception exception = assertThrows(
      ResponseStatusException.class,
      () -> tradeServiceUnderTest.save(givenTrade)
    );

    String resultMessage = exception.getMessage();

    verify(tradeRepositoryMocked,times(0)).save(givenTrade);
    assertThat(resultMessage.contains(expectedMessage)).isTrue();
  }

  @Test
  public void saveTest() {
    when(tradeRepositoryMocked.save(givenTrade)).thenReturn(givenTrade);

    Trade result = tradeServiceUnderTest.save(givenTrade);

    verify(tradeRepositoryMocked,times(1)).save(givenTrade);
    assertThat(result).isEqualTo(givenTrade);
    assertThat(result.getCreationDate()).isNotNull();
  }

  @Test
  public void findByIdTest() {
    Integer givenId = 28;

    when(tradeRepositoryMocked.findById(givenId)).thenReturn(Optional.of(givenTrade));

    Optional<Trade> result = tradeServiceUnderTest.findById(givenId);

    verify(tradeRepositoryMocked, times(1)).findById(givenId);
    assertThat(result.isPresent()).isTrue();
    assertThat(result.get()).isEqualTo(givenTrade);
  }

  @Test
  public void updateExceptionTest() {
    Trade modifiedTrade = new Trade();
    modifiedTrade.setTradeId(28);

    String expectedMessage = "Trades ID mismatch.";
    Exception exception = assertThrows(
      RuntimeException.class,
      () -> tradeServiceUnderTest.update(modifiedTrade, givenTrade)
    );

    String resultMessage = exception.getMessage();

    assertThat(resultMessage.contains(expectedMessage)).isTrue();
  }

  @Test
  public void updateTest() {
    Integer givenId = 28;
    givenTrade.setTradeId(givenId);

    String newGivenAccount = "someAccount";
    String newGivenType = "someType";
    Double newGivenBuyQuantity = 1.0d;
    Trade modifiedTrade = new Trade();
    modifiedTrade.setTradeId(givenId);
    modifiedTrade.setAccount(newGivenAccount);
    modifiedTrade.setType(newGivenType);
    modifiedTrade.setBuyQuantity(newGivenBuyQuantity);

    when(tradeRepositoryMocked.save(givenTrade)).thenReturn(givenTrade);

    Trade result = tradeServiceUnderTest.update(modifiedTrade, givenTrade);

    verify(tradeRepositoryMocked,times(1)).save(givenTrade);
    assertThat(result.getAccount()).isEqualTo(newGivenAccount);
    assertThat(result.getType()).isEqualTo(newGivenType);
    assertThat(result.getBuyQuantity()).isEqualTo(newGivenBuyQuantity);
  }

  @Test
  public void deleteTest() {
    tradeServiceUnderTest.delete(givenTrade);

    verify(tradeRepositoryMocked,times(1)).delete(givenTrade);
  }

}
