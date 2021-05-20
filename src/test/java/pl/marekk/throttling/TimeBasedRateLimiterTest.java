package pl.marekk.throttling;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class TimeBasedRateLimiterTest {

  @Test
  @SneakyThrows
  void throttlingForResourceTest() {
    // given
    final TimeUnit intervalUnit = TimeUnit.SECONDS;
    final RateLimiter twoTimesPerSecondLimiter =
        RateLimiter.timeBasedRateLimiter(2, 1, intervalUnit);
    final String resource = "method1";
    // when first shot
    assertThat(twoTimesPerSecondLimiter.isLimitExceeded(resource)).isFalse();

    // and second shot
    assertThat(twoTimesPerSecondLimiter.isLimitExceeded(resource)).isFalse();

    // then limit exceeded
    assertThat(twoTimesPerSecondLimiter.isLimitExceeded(resource)).isTrue();

    // again
    assertThat(twoTimesPerSecondLimiter.isLimitExceeded(resource)).isTrue();
    // and after second request are valid
    intervalUnit.sleep(1);
    assertThat(twoTimesPerSecondLimiter.isLimitExceeded(resource)).isFalse();
  }
}
