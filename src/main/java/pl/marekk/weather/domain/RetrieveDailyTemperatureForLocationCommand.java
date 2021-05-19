package pl.marekk.weather.domain;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveDailyTemperatureForLocationCommand {
  private static final ThreeHoursChunksLimitCalculator chunkLimitCalculator =
      new ThreeHoursChunksLimitCalculator();
  WeatherChunksLimit chunksLimit;
  String locationId;
  TemperatureUnit temperatureUnit;

  public static RetrieveDailyTemperatureForLocationCommand retrieveTomorrowTemperatureCommand(
      LocalDateTime startTime, String locationId, TemperatureUnit temperatureUnit) {
    return new RetrieveDailyTemperatureForLocationCommand(
        chunkLimitCalculator.apply(startTime), locationId, temperatureUnit);
  }

  public static RetrieveDailyTemperatureForLocationCommand retrieve5DaysTemperatureCommand(
      String locationId, TemperatureUnit temperatureUnit) {
    return new RetrieveDailyTemperatureForLocationCommand(null, locationId, temperatureUnit);
  }

  public int chunksNumberToSkip() {
    return chunksLimit == null ? 0 : chunksLimit.getOffset();
  }

  public Optional<Integer> chunksNumberToFetch() {
    if (chunksLimit == null) {
      return Optional.empty();
    }
    return Optional.of(chunksLimit.getLimit() + chunksLimit.getOffset());
  }
}
