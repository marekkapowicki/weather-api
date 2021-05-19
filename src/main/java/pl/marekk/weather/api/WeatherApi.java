package pl.marekk.weather.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.marekk.weather.domain.TemperatureUnit;

public interface WeatherApi {
  @GetMapping(path = "api/weather/summary", produces = APPLICATION_JSON_VALUE)
  LocationsDto filterLocationsWithTemperatureAbove(
      @Valid @NotEmpty @RequestParam(value = "locations") List<String> locations,
      @Valid @NotNull @RequestParam(value = "unit") TemperatureUnit temperatureUnit,
      @RequestParam(value = "minTemperature", defaultValue = "10.0") Double minTemperature);

  @GetMapping(path = "api/weather/locations/{locationId}", produces = APPLICATION_JSON_VALUE)
  TemperatureInLocationDto fetchTemperatureForLocation(
      @Valid @NotEmpty @PathVariable("locationId") String locationId,
      @Valid @NotNull @RequestParam(value = "unit", defaultValue = "CELCIUS")
          TemperatureUnit temperatureUnit);
}
