package pl.marekk.weather.application;

import static pl.marekk.weather.domain.LocationTemperatureForecast.createLocationTemperatureForecast;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pl.marekk.weather.domain.DailyTemperatureForecastVO;
import pl.marekk.weather.domain.LocationTemperatureForecast;
import pl.marekk.weather.domain.RetrieveDailyTemperatureForLocationCommand;

@AllArgsConstructor
@Slf4j
class TemperatureForecastFactory {
  private final WeatherApiClient weatherApiClient;

  LocationTemperatureForecast createTemperatureForecast(
      @NonNull RetrieveDailyTemperatureForLocationCommand command) {
    LOG.info("retrieving the temperature forecast for {}", command);
    final List<DailyTemperatureForecastVO> dailyTemperatureForecasts =
        weatherApiClient.call(command).toDailyTemperatureForecastVO(command.chunksNumberToSkip());
    return createLocationTemperatureForecast(
        command.getLocationId(), command.getTemperatureUnit(), dailyTemperatureForecasts);
  }
}
