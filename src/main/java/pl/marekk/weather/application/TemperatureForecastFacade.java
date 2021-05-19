package pl.marekk.weather.application;

import java.util.List;
import lombok.NonNull;
import pl.marekk.weather.domain.LocationTemperatureForecast;

public interface TemperatureForecastFacade {

  List<LocationTemperatureForecast> locationsWithTomorrowTemperatureHigherThan(
      @NonNull RetrieveTomorrowTemperatureCommand command);
}
