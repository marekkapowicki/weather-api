package pl.marekk.weather.domain;

import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
@Slf4j
public class DailyTemperatureForecastVO {
  LocalDate forecastDate;
  List<TemperatureChunkVO> temperatureChunks;

  public static DailyTemperatureForecastVO dailyTemperatureForecast(
      @NonNull LocalDate forecastDate, @NonNull List<TemperatureChunkVO> temperatureChunks) {

    return new DailyTemperatureForecastVO(forecastDate, temperatureChunks);
  }

  boolean temperatureIsAbove(double minTemp) {
    return temperatureChunks.stream()
        .map(TemperatureChunkVO::getMinTemperature)
        .allMatch(chunkMinTemp -> minTemp <= chunkMinTemp);
  }
}
