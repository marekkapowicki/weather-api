package pl.marekk.weather.api;

import static pl.marekk.weather.application.RetrieveTomorrowTemperatureCommand.createRetrieveTomorrowTemperatureCommand;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import pl.marekk.weather.application.RetrieveTomorrowTemperatureCommand;
import pl.marekk.weather.application.TemperatureForecastFacade;
import pl.marekk.weather.domain.LocationTemperatureForecast;
import pl.marekk.weather.domain.TemperatureUnit;

@RestController
@AllArgsConstructor
@Slf4j
public class WeatherApiController implements WeatherApi {
  private final TemperatureForecastFacade temperatureForecastFacade;

  @Override
  public LocationsDto filterLocationsWithTemperatureAbove(
      List<String> locations, TemperatureUnit temperatureUnit, Double minTemperature) {
    LOG.info("searching the locations with temperature above {} in {}", minTemperature, locations);
    final RetrieveTomorrowTemperatureCommand retrieveTomorrowTemperatureCommand =
        createRetrieveTomorrowTemperatureCommand(locations, temperatureUnit, minTemperature);
    final List<LocationTemperatureForecast> locationTemperatureForecasts =
        temperatureForecastFacade.locationsWithTomorrowTemperatureHigherThan(
            retrieveTomorrowTemperatureCommand);
    return LocationsDto.fromDomainObject(locationTemperatureForecasts);
  }
}
