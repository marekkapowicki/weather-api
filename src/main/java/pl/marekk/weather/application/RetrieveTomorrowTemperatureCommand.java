package pl.marekk.weather.application;

import static pl.marekk.weather.domain.RetrieveDailyTemperatureForLocationCommand.retrieveDailyTemperatureCommand;

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
public class RetrieveTomorrowTemperatureCommand {
  private final List<String> locationIds;
  private final TemperatureUnit temperatureUnit;
  private final LocalDateTime currentTime;
  private final double minTemp;

  public static RetrieveTomorrowTemperatureCommand createRetrieveTomorrowTemperatureCommand(
      @NonNull List<String> locationIds, @NonNull TemperatureUnit temperatureUnit, double minTemp) {
    return new RetrieveTomorrowTemperatureCommand(
        locationIds, temperatureUnit, LocalDateTime.now(), minTemp);
  }

  List<RetrieveDailyTemperatureForLocationCommand> dailyTemperaturePerLocationCommands() {
    return locationIds.stream()
        .map(location -> retrieveDailyTemperatureCommand(currentTime, location, temperatureUnit))
        .collect(Collectors.toList());
  }
}
