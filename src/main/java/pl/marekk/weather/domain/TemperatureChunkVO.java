package pl.marekk.weather.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.apache.commons.lang3.time.DurationFormatUtils;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TemperatureChunkVO {
  LocalDateTime startTime;

  @Getter(value = AccessLevel.NONE)
  Duration duration;

  double minTemperature;
  double maxTemperature;

  public static TemperatureChunkVO threeHoursChunk(
      LocalDateTime startTime, double minTemperature, double maxTemperature) {
    return new TemperatureChunkVO(startTime, Duration.ofHours(3), minTemperature, maxTemperature);
  }

  public String getDuration() {
    final String formatDuration = DurationFormatUtils.formatDuration(this.duration.toMillis(), "H:mm", true);
    return formatDuration + "(H:mm)";
  }
}
