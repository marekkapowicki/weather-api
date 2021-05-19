package pl.marekk.weather.domain;

import lombok.Value;

@Value
class WeatherChunksLimit {
  private static final int TREE_HOURS_SLOTS_PER_DAY_LIMIT = 8;
  int offset;
  int limit;

  static WeatherChunksLimit threeHoursChunksLimit(int offset) {
    return new WeatherChunksLimit(offset, TREE_HOURS_SLOTS_PER_DAY_LIMIT);
  }
}
