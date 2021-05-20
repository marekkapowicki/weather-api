package pl.marekk.throttling;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RateLimiterProperties.class)
public class ThrottlingConfiguration {

  @Bean
  RateLimiter rateLimiter(RateLimiterProperties properties) {
    return RateLimiter.timeBasedRateLimiter(
        properties.getRequestsLimit(), properties.getInterval(), properties.getIntervalUnit());
  }
}
