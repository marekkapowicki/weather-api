package pl.marekk.weather.domain;

import java.time.LocalDateTime;
import java.util.function.Function;
import lombok.NonNull;

/** calculate the number of 3 hours slots to fetch from api to take the weather for next day */
public class ThreeHoursChunksLimitCalculator
    implements Function<LocalDateTime, WeatherChunksLimit> {

  @Override
  public WeatherChunksLimit apply(@NonNull LocalDateTime localDateTime) {
    final int currentHour = localDateTime.getHour();
    int chunksLimit = (24 - currentHour) / 3;
    return WeatherChunksLimit.threeHoursChunksLimit(chunksLimit);
  }
}
