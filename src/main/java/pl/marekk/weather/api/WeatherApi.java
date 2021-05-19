package pl.marekk.weather.api;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.marekk.weather.domain.TemperatureUnit;

public interface WeatherApi {
  @GetMapping(path = "api/weather/summary", produces = MediaType.APPLICATION_JSON_VALUE)
  LocationsDto filterLocationsWithTemperatureAbove(
      @Valid @NotEmpty @RequestParam(value = "locations") List<String> locations,
      @Valid @NotNull @RequestParam(value = "unit") TemperatureUnit temperatureUnit,
      @RequestParam(value = "minTemperature", defaultValue = "10.0") Double minTemperature);
}
