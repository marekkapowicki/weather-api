package pl.marekk.weather.api;

import static pl.marekk.weather.application.RetrieveTemperatureCommand.createRetrieveTemperatureCommand;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import pl.marekk.throttling.RateLimiter;
import pl.marekk.weather.application.RetrieveTemperatureCommand;
import pl.marekk.weather.application.TemperatureForecastFacade;
import pl.marekk.weather.domain.LocationTemperatureForecast;
import pl.marekk.weather.domain.TemperatureUnit;
import pl.marekk.weather.exception.Exceptions;

@RestController
@AllArgsConstructor
@Slf4j
public class WeatherApiController implements WeatherApi {
  private final TemperatureForecastFacade temperatureForecastFacade;
  private final RateLimiter rateLimiter;

  @Override
  public LocationsDto filterLocationsWithTemperatureAbove(
      List<String> locations, TemperatureUnit temperatureUnit, Double minTemperature) {
    LOG.info("searching the locations with temperature above {} in {}", minTemperature, locations);

    checkRequestsLimit();
    final RetrieveTemperatureCommand retrieveTemperatureCommand =
        createRetrieveTemperatureCommand(locations, temperatureUnit, minTemperature);
    final List<LocationTemperatureForecast> locationTemperatureForecasts =
        temperatureForecastFacade.filterLocationsWithTomorrowTemperatureHigherThan(
            retrieveTemperatureCommand);
    return LocationsDto.fromDomainObject(locationTemperatureForecasts);
  }

  @Override
  public TemperatureInLocationDto fetchTemperatureForLocation(
      String locationId, TemperatureUnit temperatureUnit) {
    checkRequestsLimit();
    LOG.info("checking the 5 days temperature forecast in {}", locationId);
    final LocationTemperatureForecast locationTemperatureForecast =
        temperatureForecastFacade.fetchTemperatureForecastFor(locationId, temperatureUnit);
    return TemperatureInLocationDto.fromDomainObject(locationTemperatureForecast);
  }

  // TODO use as annotation
  private void checkRequestsLimit() {
    if (rateLimiter.isLimitExceeded("weather-api")) {
      throw Exceptions.requestsLimitExceeded();
    }
  }
}
