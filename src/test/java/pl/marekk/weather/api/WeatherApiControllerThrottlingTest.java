package pl.marekk.weather.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.config.EncoderConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.response.ResponseOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import pl.marekk.throttling.RateLimiter;
import pl.marekk.weather.application.RetrieveTemperatureCommand;
import pl.marekk.weather.application.TemperatureForecastFacade;
import pl.marekk.weather.domain.LocationTemperatureForecast;
import pl.marekk.weather.domain.TemperatureUnit;

@SpringBootTest(classes = WeatherApiControllerThrottlingTest.Config.class)
class WeatherApiControllerThrottlingTest {

  @Autowired WebApplicationContext webApplicationContext;

  @BeforeEach
  public void setup() {
    // remove::start[]
    EncoderConfig encoderConfig =
        new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false);
    RestAssuredMockMvc.config = new RestAssuredMockMvcConfig().encoderConfig(encoderConfig);
    RestAssuredMockMvc.webAppContextSetup(this.webApplicationContext);
    // remove::end[]
  }

  @Test
  void limitExceededIn3rdShot() {
    // given
    MockMvcRequestSpecification request = given();
    // 1st shot
    final String url =
        "/api/weather/summary?locations=123&locations=345&minTemperature=26&unit=CELCIUS";
    ResponseOptions<MockMvcResponse> response1 = given().spec(request).get(url);
    assertThat(response1.statusCode()).isEqualTo(200);
    // and 2nd shot
    ResponseOptions<MockMvcResponse> response2 = given().spec(request).get(url);
    assertThat(response2.statusCode()).isEqualTo(200);
    // then limit exceeded
    ResponseOptions<MockMvcResponse> response3 = given().spec(request).get(url);
    assertThat(response3.statusCode()).isEqualTo(429);
  }

  @Configuration
  @EnableAutoConfiguration
  static class Config {

    @Bean
    WeatherApiController controller() {
      return new WeatherApiController(temperatureForecastFacade(), rateLimiter());
    }

    @Bean
    RateLimiter rateLimiter() {
      return new MockTwoShotsRateLimiter();
    }

    @Bean
    TemperatureForecastFacade temperatureForecastFacade() {
      return new MockFacade();
    }

    private static class MockFacade implements TemperatureForecastFacade {

      @Override
      public List<LocationTemperatureForecast> filterLocationsWithTomorrowTemperatureHigherThan(
          @NonNull RetrieveTemperatureCommand command) {
        return new ArrayList<>();
      }

      // TODO implement it for tests
      @Override
      public LocationTemperatureForecast fetchTemperatureForecastFor(
          String locationId, TemperatureUnit temperatureUnit) {
        return null;
      }
    }

    private static class MockTwoShotsRateLimiter implements RateLimiter {

      private final LongAdder counter = new LongAdder();
      private int maxLimit = 2;

      @Override
      public boolean isLimitExceeded(String resourceName) {
        counter.increment();
        return counter.intValue() > maxLimit;
      }
    }
  }
}
