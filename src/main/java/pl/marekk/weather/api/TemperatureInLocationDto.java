package pl.marekk.weather.api;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.Value;
import pl.marekk.weather.domain.DailyTemperatureForecastVO;
import pl.marekk.weather.domain.LocationTemperatureForecast;
import pl.marekk.weather.domain.TemperatureChunkVO;
import pl.marekk.weather.domain.TemperatureUnit;

@Value
class TemperatureInLocationDto {

  String locationId;
  TemperatureUnit temperatureUnit;
  List<DailyForecastDto> dailyForecasts;

  public static TemperatureInLocationDto fromDomainObject(
      @NonNull LocationTemperatureForecast locationTemperatureForecast) {
    final List<DailyForecastDto> dailyForecastDtos =
        locationTemperatureForecast.getDailyTemperatureForecasts().stream()
            .map(DailyForecastDto::fromVO)
            .collect(Collectors.toList());
    return new TemperatureInLocationDto(
        locationTemperatureForecast.getLocationId(),
        locationTemperatureForecast.getTemperatureUnit(),
        dailyForecastDtos);
  }

  @Value
  static class DailyForecastDto {
    LocalDate date;
    List<PeriodForecastDto> periods;

    static DailyForecastDto fromVO(@NonNull DailyTemperatureForecastVO vo) {
      final List<PeriodForecastDto> forecastDtos =
          vo.getTemperatureChunks().stream()
              .map(PeriodForecastDto::fromVO)
              .collect(Collectors.toList());
      return new DailyForecastDto(vo.getForecastDate(), forecastDtos);
    }
  }

  @Value
  static class PeriodForecastDto {
    LocalTime startTime;
    String duration;
    double minTemp;
    double maxTemp;

    static PeriodForecastDto fromVO(@NonNull TemperatureChunkVO vo) {
      return new PeriodForecastDto(
          vo.getStartTime().toLocalTime(),
          vo.getDuration(),
          vo.getMinTemperature(),
          vo.getMaxTemperature());
    }
  }
}
