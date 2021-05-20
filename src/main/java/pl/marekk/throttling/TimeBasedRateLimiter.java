package pl.marekk.throttling;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
class TimeBasedRateLimiter implements RateLimiter {
  private final int maxRequestsNumber;
  private final int interval;
  private final TimeUnit intervalUnit;
  private final Map<String, LoadingCache<Long, Long>> storage;

  static TimeBasedRateLimiter timeBasedRateLimiter(
      final int maxRequestsNumber, final int interval, final TimeUnit intervalUnit) {
    return new TimeBasedRateLimiter(
        maxRequestsNumber, interval, intervalUnit, new ConcurrentHashMap<>());
  }

  @Override
  public boolean isLimitExceeded(String resourceName) {
    Cache<Long, Long> autoExpiringUserCallsCounter =
        storage.computeIfAbsent(resourceName, k -> buildCacheWhichRemovesEntriesAfterTimeFrame());
    Long callsCount = autoExpiringUserCallsCounter.size();
    if (requestLimitReached(callsCount)) {
      autoExpiringUserCallsCounter.cleanUp();
      if (requestLimitReached(autoExpiringUserCallsCounter.size())) {
        LOG.debug("request limit exceeded for {}", resourceName);
        return true;
      }
      return false;

    } else {
      long randomKeyToIncreaseCounter = new SecureRandom().nextLong();
      autoExpiringUserCallsCounter.put(randomKeyToIncreaseCounter, randomKeyToIncreaseCounter);
      return false;
    }
  }

  private boolean requestLimitReached(Long callsCount) {
    return callsCount != null && callsCount + 1 > maxRequestsNumber;
  }

  private LoadingCache<Long, Long> buildCacheWhichRemovesEntriesAfterTimeFrame() {
    return CacheBuilder.newBuilder()
        .removalListener(notification -> LOG.debug(notification.getKey() + " removed"))
        .refreshAfterWrite(interval, intervalUnit)
        .expireAfterWrite(interval, intervalUnit)
        .build(
            new CacheLoader<>() {
              @Override
              public Long load(Long key) {
                return 0L;
              }
            });
  }
}
