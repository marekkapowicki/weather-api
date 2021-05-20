package pl.marekk.throttling;

import java.util.concurrent.TimeUnit;

public interface RateLimiter {
  static RateLimiter timeBasedRateLimiter(int requestsLimit, int interval, TimeUnit intervalUnit) {
    return TimeBasedRateLimiter.timeBasedRateLimiter(requestsLimit, interval, intervalUnit);
  }

  boolean isLimitExceeded(String resourceName);
}
