package pl.marekk.weather.application;

import java.util.concurrent.TimeUnit;
import javax.validation.constraints.Min;
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

  @NotNull
  private MaxRequestsLimit maxRequests;

  @Data
  @Validated
  private static class MaxRequestsLimit {
    @NotNull
    @Min(1)
    private int number;

    @NotNull
    private TimeUnit unit;
  }
}
