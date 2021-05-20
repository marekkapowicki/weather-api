package pl.marekk.weather.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.marekk.weather.domain.LocationTemperatureForecast.createLocationTemperatureForecast;
import static pl.marekk.weather.domain.TemperatureUnit.CELCIUS;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.toomuchcoding.jsonassert.JsonAssertion;
import io.restassured.config.EncoderConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.response.ResponseOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
import pl.marekk.weather.exception.Exceptions;

@SpringBootTest(classes = WeatherApiControllerTest.Config.class)
class WeatherApiControllerTest {

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
  void takeLocationsWithTemperatureAboveHappyPath() {
    // given
    MockMvcRequestSpecification request = given();
    // when
    ResponseOptions<MockMvcResponse> response =
        given()
            .spec(request)
            .get("/api/weather/summary?locations=123&locations=345&minTemperature=26&unit=CELCIUS");
    // then
    assertThat(response.statusCode()).isEqualTo(200);
    // and
    DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
    JsonAssertion.assertThatJson(parsedJson)
        .field("['locationIds']")
        .read(List.class)
        .equals(List.of("123", "345"));
  }

  @Test
  void takeLocationsWithTemperatureAboveExceptionalPath() {
    // given
    MockMvcRequestSpecification request = given();
    // when
    ResponseOptions<MockMvcResponse> response =
        given()
            .spec(request)
            .get("/api/weather/summary?locations=666&locations=345&minTemperature=26&unit=CELCIUS");
    // then:
    assertThat(response.statusCode()).isEqualTo(500);
  }

  @Configuration
  @EnableAutoConfiguration
  static class Config {

    @Bean
    WeatherApiController controller() {
      return new WeatherApiController(temperatureForecastFacade(), rateLimiter());
    }

    @Bean
    RateLimiter rateLimiter(){
      return new MockRateLimiter();
    }
    @Bean
    TemperatureForecastFacade temperatureForecastFacade() {
      return new MockFacade();
    }

    private static class MockFacade implements TemperatureForecastFacade {

      @Override
      public List<LocationTemperatureForecast> filterLocationsWithTomorrowTemperatureHigherThan(
          @NonNull RetrieveTemperatureCommand command) {
        if (command.getLocationIds().contains("666")) {
          throw Exceptions.illegalState("fake exception");
        }
        return command.getLocationIds().stream()
            .map(
                location -> createLocationTemperatureForecast(location, CELCIUS, new ArrayList<>()))
            .collect(Collectors.toList());
      }

      // TODO implement it for tests
      @Override
      public LocationTemperatureForecast fetchTemperatureForecastFor(
          String locationId, TemperatureUnit temperatureUnit) {
        return null;
      }
    }

    private static class MockRateLimiter implements RateLimiter {

      @Override
      public boolean limitIsExceeded(String resourceName) {
        return false;
      }
    }
  }
}
