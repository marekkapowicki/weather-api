package pl.marekk.throttling;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
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
  private final Storage storage;

  static TimeBasedRateLimiter timeBasedRateLimiter(
      final int maxRequestsNumber, final int interval, final TimeUnit intervalUnit) {
    return new TimeBasedRateLimiter(
        maxRequestsNumber, Storage.storage(interval, intervalUnit));
  }

  @Override
  public boolean limitIsExceeded(String resourceName) {
    Long callsCount = storage.takeCallsCount(resourceName);
    if (requestLimitReached(callsCount)) {
      LOG.debug("request limit exceeded for {}", resourceName);
      return true;
    } else {
      storage.increaseCounter(resourceName);
      return false;
    }
  }

  private boolean requestLimitReached(Long callsCount) {
    return callsCount != null && callsCount + 1 > maxRequestsNumber;
  }

  @AllArgsConstructor
  private static final class Storage {
    private final Map<String, Cache<Long, Long>> cacheStorage;
    private final int interval;
    private final TimeUnit intervalUnit;

    static Storage storage(int interval, TimeUnit intervalUnit) {

      return new Storage(new ConcurrentHashMap<>(), interval, intervalUnit);
    }

    private static Cache<Long, Long> buildCacheWhichRemovesEntriesAfterTimeFrame(
        int interval, TimeUnit intervalUnit) {
      return CacheBuilder.newBuilder()
          .concurrencyLevel(5)
          .expireAfterWrite(interval, intervalUnit)
          .build();
    }

    void increaseCounter(String key) {
      long randomKeyToIncreaseCounter = new SecureRandom().nextLong();
      getAutoExpiringUserCallsCounter(key)
          .put(randomKeyToIncreaseCounter, randomKeyToIncreaseCounter);
    }

    long takeCallsCount(String key) {
      Cache<Long, Long> autoExpiringUserCallsCounter = getAutoExpiringUserCallsCounter(key);
      return autoExpiringUserCallsCounter.size();
    }

    private Cache<Long, Long> getAutoExpiringUserCallsCounter(String key) {
      return cacheStorage.computeIfAbsent(
          key, k -> buildCacheWhichRemovesEntriesAfterTimeFrame(interval, intervalUnit));
    }
  }
}
