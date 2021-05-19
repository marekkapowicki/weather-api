package pl.marekk.weather.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static pl.marekk.weather.application.TemperatureDailyCommands.sampleDailyCommand;

import java.util.List;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.groups.Tuple;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
import pl.marekk.weather.LocalApiTest;
import pl.marekk.weather.Resources;
import pl.marekk.weather.application.WeatherResponse.ChunkPart;
import pl.marekk.weather.application.WeatherResponse.MainPart;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@LocalApiTest
class LocalWeatherApiClientTest {
  private static final MockWebServer WEB_SERVER = new MockWebServer();
  @Autowired private WeatherApiClient weatherApiClient;

  @BeforeAll
  @SneakyThrows
  static void beforeAll() {
    WEB_SERVER.start(9091);
  }

  @AfterAll
  @SneakyThrows
  static void afterAll() {
    WEB_SERVER.close();
  }

  @Test
  void fetchTheWeatherHappyPath() {
    // given
    final String fakeResponse = Resources.loadResource("mock_single_day_response.json");
    MockResponse response = mockResponse(fakeResponse);
    WEB_SERVER.enqueue(response);
    // when
    final WeatherResponse weatherResponse = weatherApiClient.call(sampleDailyCommand);

    // then
    assertThat(weatherResponse.getCod()).isNotNull();
    final List<ChunkPart> chunkParts = weatherResponse.getList();
    assertThat(chunkParts)
        .isNotEmpty()
        .extracting(ChunkPart::getMain)
        .extracting(MainPart::getTempMin, MainPart::getTempMax)
        .contains(Tuple.tuple(285.04, 285.04));
    assertThat(chunkParts).isNotEmpty().extracting(ChunkPart::getDt).doesNotContainNull();
  }

  @Test
  void apiError404Response() {
    // given
    final String errorResponse =
        "{\n" + "  \"cod\": \"404\",\n" + "  \"message\": \"city not found\"\n" + "}";
    final MockResponse mockResponse = mockResponse(errorResponse).setResponseCode(404);
    WEB_SERVER.enqueue(mockResponse);
    // expect
    assertThatExceptionOfType(ResponseStatusException.class)
        .isThrownBy(() -> weatherApiClient.call(sampleDailyCommand))
        .withMessage("404 NOT_FOUND \"city not found\"");
  }

  @NotNull
  private MockResponse mockResponse(String fakeResponse) {
    return new MockResponse()
        .addHeader("Content-Type", "application/json; charset=utf-8")
        .addHeader("Cache-Control", "no-cache")
        .setBody(fakeResponse);
  }
}
