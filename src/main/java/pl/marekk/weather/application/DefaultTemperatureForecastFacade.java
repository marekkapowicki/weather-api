package pl.marekk.weather.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;
import pl.marekk.weather.domain.LocationTemperatureForecast;
import pl.marekk.weather.domain.RetrieveDailyTemperatureForLocationCommand;
import pl.marekk.weather.domain.TemperatureUnit;
import pl.marekk.weather.exception.Exceptions;

@AllArgsConstructor
@Slf4j
class DefaultTemperatureForecastFacade implements TemperatureForecastFacade {
  private final TemperatureForecastFactory temperatureForecastFactory;
  private final RequestsLimiter requestsLimiter;

  @Override
  public List<LocationTemperatureForecast> filterLocationsWithTomorrowTemperatureHigherThan(
      @NonNull RetrieveTemperatureCommand command) {
    try {
      LOG.info(
          "finding the locations with tomorrow temperature above {} in the given location list {}",
          command.getMinTemp(),
          command.getLocationIds());
      requestsLimiter.verifyRequestNumbers();
      return tryFilterLocationsWithTomorrowTemperatureHigherThan(command);
    } catch (ResponseStatusException e) {
      LOG.error("error was thrown", e);
      throw e;
    } catch (RuntimeException e) {
      LOG.error("unknown runtime exception", e);
      throw Exceptions.illegalState(e.getMessage());
    }
  }

  @Override
  public LocationTemperatureForecast fetchTemperatureForecastFor(
      @NonNull String locationId, @NonNull TemperatureUnit temperatureUnit) {
    try {
      requestsLimiter.verifyRequestNumbers();
      return tryFetchTemperatureForecastFor(locationId, temperatureUnit);
    } catch (ResponseStatusException e) {
      LOG.error("error was thrown", e);
      throw e;
    } catch (RuntimeException e) {
      LOG.error("unknown runtime exception", e);
      throw Exceptions.illegalState(e.getMessage());
    }
  }

  private List<LocationTemperatureForecast> tryFilterLocationsWithTomorrowTemperatureHigherThan(
      RetrieveTemperatureCommand command) {
    return command.retrieveTomorrowTemperaturePerLocationCommands().stream()
        .map(temperatureForecastFactory::createTemperatureForecast)
        .map(location -> location.takeIfTemperatureIsAbove(command.getMinTemp()))
        .flatMap(Optional::stream)
        .collect(Collectors.toList());
  }

  private LocationTemperatureForecast tryFetchTemperatureForecastFor(
      String locationId, TemperatureUnit temperatureUnit) {
    final RetrieveDailyTemperatureForLocationCommand retrieveDailyTemperatureForLocationCommand =
        RetrieveDailyTemperatureForLocationCommand.retrieve5DaysTemperatureCommand(
            locationId, temperatureUnit);
    return temperatureForecastFactory.createTemperatureForecast(
        retrieveDailyTemperatureForLocationCommand);
  }
}
