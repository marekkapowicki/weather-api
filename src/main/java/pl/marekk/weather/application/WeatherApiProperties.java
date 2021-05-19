package pl.marekk.weather.application;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "weather")
@Validated
@Data
public class WeatherApiProperties {
  @NotNull private String url;

  @NotNull private String key;
}
