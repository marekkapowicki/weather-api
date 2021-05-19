package pl.marekk.weather.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TemperatureUnit {
  CELCIUS("metric"),
  FARENHEIT("imperial");
  private final String temperatureName;
}
