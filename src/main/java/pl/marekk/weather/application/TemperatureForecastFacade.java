package pl.marekk.weather.application;

import java.util.List;
import lombok.NonNull;
import pl.marekk.weather.domain.LocationTemperatureForecast;
import pl.marekk.weather.domain.TemperatureUnit;

public interface TemperatureForecastFacade {

  List<LocationTemperatureForecast> filterLocationsWithTomorrowTemperatureHigherThan(
      @NonNull RetrieveTemperatureCommand command);

  LocationTemperatureForecast fetchTemperatureForecastFor(
      String locationId, TemperatureUnit temperatureUnit);
}
