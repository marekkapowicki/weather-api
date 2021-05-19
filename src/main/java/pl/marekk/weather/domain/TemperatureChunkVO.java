package pl.marekk.weather.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TemperatureChunkVO {
  LocalDateTime startTime;
  Duration duration;
  double minTemperature;
  double maxTemperature;

  public static TemperatureChunkVO threeHoursChunk(
      LocalDateTime startTime, double minTemperature, double maxTemperature) {
    return new TemperatureChunkVO(startTime, Duration.ofHours(3), minTemperature, maxTemperature);
  }
}
