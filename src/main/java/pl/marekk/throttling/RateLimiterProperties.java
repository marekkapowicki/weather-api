package pl.marekk.throttling;

import java.util.concurrent.TimeUnit;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "rate-limiter")
class RateLimiterProperties {
  @Min(1)
  private int requestsLimit;

  @Min(1)
  private int interval;

  @NotNull
  private TimeUnit intervalUnit;
}
