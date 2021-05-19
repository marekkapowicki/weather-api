package pl.marekk.weather.domain;

import java.time.LocalDateTime;
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
  LocalDateTime startTime;
  TemperatureUnit temperatureUnit;

  public static RetrieveDailyTemperatureForLocationCommand retrieveDailyTemperatureCommand(
      LocalDateTime startTime, String locationId, TemperatureUnit temperatureUnit) {
    return new RetrieveDailyTemperatureForLocationCommand(
        chunkLimitCalculator.apply(startTime), locationId, startTime, temperatureUnit);
  }

  public int chunksNumberToSkip() {
    return chunksLimit.getOffset();
  }

  public String chunksNumberToFetch() {
    final int numberToFetch = chunksLimit.getLimit() + chunksLimit.getOffset();
    return String.valueOf(numberToFetch);
  }
}
