package pl.marekk.weather.api;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Value;
import pl.marekk.weather.domain.LocationTemperatureForecast;

@Value
class LocationsDto {
  List<String> locationIds;

  static LocationsDto fromDomainObject(
      List<LocationTemperatureForecast> locationTemperatureForecasts) {
    final List<String> locations =
        locationTemperatureForecasts.stream()
            .map(LocationTemperatureForecast::getLocationId)
            .collect(Collectors.toList());
    return new LocationsDto(locations);
  }
}
