package pl.marekk.weather.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pl.marekk.weather.domain.LocationTemperatureForecast;

@AllArgsConstructor
@Slf4j
class DefaultTemperatureForecastFacade implements TemperatureForecastFacade {
  private final TemperatureForecastFactory temperatureForecastFactory;
  @Override
  public List<LocationTemperatureForecast> locationsWithTomorrowTemperatureHigherThan(
      @NonNull RetrieveTomorrowTemperatureCommand command) {
    LOG.info(
        "finding the locations with tomorrow temperature above {} in the given location list {}",
        command.getMinTemp(),
        command.getLocationIds());
    return command.dailyTemperaturePerLocationCommands().stream()
        .map(temperatureForecastFactory::createTemperatureForecast)
        .map(location -> location.takeIfTemperatureIsAbove(command.getMinTemp()))
        .flatMap(Optional::stream)
        .collect(Collectors.toList());
  }
}
