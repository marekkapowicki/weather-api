package pl.marekk.weather.application;

import static pl.marekk.weather.domain.RetrieveDailyTemperatureForLocationCommand.retrieveTomorrowTemperatureCommand;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import pl.marekk.weather.domain.RetrieveDailyTemperatureForLocationCommand;
import pl.marekk.weather.domain.TemperatureUnit;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class RetrieveTemperatureCommand {
  private final List<String> locationIds;
  private final TemperatureUnit temperatureUnit;
  private final LocalDateTime currentTime;
  private final double minTemp;

  public static RetrieveTemperatureCommand createRetrieveTemperatureCommand(
      @NonNull List<String> locationIds, @NonNull TemperatureUnit temperatureUnit, double minTemp) {
    return new RetrieveTemperatureCommand(
        locationIds, temperatureUnit, LocalDateTime.now(), minTemp);
  }

  List<RetrieveDailyTemperatureForLocationCommand>
      retrieveTomorrowTemperaturePerLocationCommands() {
    return locationIds.stream()
        .map(location -> retrieveTomorrowTemperatureCommand(currentTime, location, temperatureUnit))
        .collect(Collectors.toList());
  }
}
