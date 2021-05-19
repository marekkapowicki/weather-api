package pl.marekk.weather.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class ThreeHoursChunksLimitCalculatorTest {

  private final ThreeHoursChunksLimitCalculator calculator = new ThreeHoursChunksLimitCalculator();

  @Test
  void calculateNumberOfChunks_for9oclock() {
    // Given
    LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0));

    // when
    final WeatherChunksLimit chunksLimit = calculator.apply(time);

    // then
    assertThat(chunksLimit.getOffset()).isEqualTo(5);
    assertThat(chunksLimit.getLimit()).isEqualTo(8);
  }

  @Test
  void calculateNumberOfChunks_for23clock() {
    // Given
    LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 0));

    // when
    final WeatherChunksLimit chunksLimit = calculator.apply(time);

    // then
    assertThat(chunksLimit.getOffset()).isZero();
    assertThat(chunksLimit.getLimit()).isEqualTo(8);
  }
}
