package pl.marekk.weather.domain;

import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
/** domain object contains location and the list of daily temperature forecasts in given unit */
public class LocationTemperatureForecast {
  @Getter private final String locationId;
  private final TemperatureUnit temperatureUnit;
  private final List<DailyTemperatureForecastVO> dailyTemperatureForecasts;

  public static LocationTemperatureForecast createLocationTemperatureForecast(
      @NonNull String locationId,
      @NonNull TemperatureUnit temperatureUnit,
      @NonNull List<DailyTemperatureForecastVO> dailyTemperatureForecasts) {
    return new LocationTemperatureForecast(locationId, temperatureUnit, dailyTemperatureForecasts);
  }

  public Optional<LocationTemperatureForecast> takeIfTemperatureIsAbove(double minTemp) {
    final boolean successTemperatureCondition =
        dailyTemperatureForecasts.stream()
            .allMatch(dailyTemperature -> dailyTemperature.temperatureIsAbove(minTemp));
    return successTemperatureCondition ? Optional.of(this) : Optional.empty();
  }
}
