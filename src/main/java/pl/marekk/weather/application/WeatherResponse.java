package pl.marekk.weather.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import pl.marekk.weather.domain.DailyTemperatureForecastVO;
import pl.marekk.weather.domain.TemperatureChunkVO;
import pl.marekk.weather.exception.Exceptions;

@Data
class WeatherResponse {
  private String cod;
  private List<ChunkPart> list;

  List<DailyTemperatureForecastVO> toDailyTemperatureForecastVO(int chunksToSkipNumber) {
    if (list == null || list.isEmpty()) {
      throw Exceptions.illegalState("no forecast fetched");
    }
    return DailyWeatherResponse.splitChunksPerDay(list, chunksToSkipNumber)
        .toDailyTemperatureForecastVO();
  }

  @Data
  static class ChunkPart {
    @Getter(value = AccessLevel.NONE)
    private long dt;

    private MainPart main;

    TemperatureChunkVO toTemperatureChunkVO() {
      return TemperatureChunkVO.threeHoursChunk(getDt(), main.tempMin, main.tempMax);
    }

    LocalDateTime getDt() {
      final Instant dtInstant = Instant.ofEpochSecond(dt);
      return LocalDateTime.ofInstant(dtInstant, ZoneOffset.UTC);
    }
  }

  @Data
  static class MainPart {
    @JsonProperty("temp_max")
    double tempMax;

    @JsonProperty("temp_min")
    double tempMin;
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  static class DailyWeatherResponse {
    private final Map<LocalDate, List<TemperatureChunkVO>> chunksPerDay;

    static DailyWeatherResponse splitChunksPerDay(
        @NonNull List<ChunkPart> chunkParts, int chunksToSkipNumber) {
      final Map<LocalDate, List<TemperatureChunkVO>> chunksPerDay =
          chunkParts.stream()
              .skip(chunksToSkipNumber)
              .map(ChunkPart::toTemperatureChunkVO)
              .collect(
                  Collectors.groupingBy(DailyWeatherResponse::getDateOfChunk, Collectors.toList()));
      return new DailyWeatherResponse(chunksPerDay);
    }

    private static LocalDate getDateOfChunk(TemperatureChunkVO vo) {
      return vo.getStartTime().toLocalDate();
    }

    List<DailyTemperatureForecastVO> toDailyTemperatureForecastVO() {
      return chunksPerDay.entrySet().stream()
          .map(
              entry ->
                  DailyTemperatureForecastVO.dailyTemperatureForecast(
                      entry.getKey(), entry.getValue()))
          .collect(Collectors.toList());
    }
  }
}
