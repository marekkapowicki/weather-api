package pl.marekk.weather.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.marekk.weather.domain.DailyTemperatureForecastVO.dailyTemperatureForecast;
import static pl.marekk.weather.domain.TemperatureChunkVO.threeHoursChunk;
import static pl.marekk.weather.domain.TemperatureUnit.CELCIUS;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

class LocationTemperatureForecastTest {

  @Test
  void temperatureIsAbove8() {
    // given
    final DailyTemperatureForecastVO temperatureForecast = sampleForecastWithMinTemperature10();
    final LocationTemperatureForecast warsawForecast =
        LocationTemperatureForecast.createLocationTemperatureForecast(
            "WarsawId", CELCIUS, List.of(temperatureForecast));
    // when
    final Optional<LocationTemperatureForecast> locationTemperatureForecast =
        warsawForecast.takeIfTemperatureIsAbove(8);
    // then
    assertThat(locationTemperatureForecast).isNotEmpty();
  }

  @Test
  void temperatureIsNotAbove12() {
    // given
    final DailyTemperatureForecastVO temperatureForecast = sampleForecastWithMinTemperature10();
    final LocationTemperatureForecast warsawForecast =
        LocationTemperatureForecast.createLocationTemperatureForecast(
            "WarsawId", CELCIUS, List.of(temperatureForecast));
    // when
    final Optional<LocationTemperatureForecast> locationTemperatureForecast =
        warsawForecast.takeIfTemperatureIsAbove(12);
    // then
    assertThat(locationTemperatureForecast).isEmpty();
  }

  @NotNull
  private DailyTemperatureForecastVO sampleForecastWithMinTemperature10() {
    return dailyTemperatureForecast(
        LocalDate.now(),
        List.of(
            threeHoursChunk(LocalDateTime.now(), 10, 18),
            threeHoursChunk(LocalDateTime.now(), 15, 15)));
  }
}
