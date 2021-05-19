package pl.marekk.weather.application;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.marekk.weather.application.TemperatureDailyCommands.sampleDailyCommand;

import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.marekk.weather.RealApiTest;
import pl.marekk.weather.application.WeatherResponse.ChunkPart;
import pl.marekk.weather.application.WeatherResponse.MainPart;

@SpringBootTest // TODO use smaller chunk than spring boot
@RealApiTest
//@ActiveProfiles("prod")
class RealWeatherApiClientTest {
  @Autowired private WeatherApiClient weatherApiClient;

  @Test
  void fetchTheWeatherHappyPath() {

    // when
    final WeatherResponse weatherResponse = weatherApiClient.call(sampleDailyCommand);

    // then
    assertThat(weatherResponse.getCod()).isNotNull();
    final List<ChunkPart> chunkParts = weatherResponse.getList();
    assertThat(chunkParts)
        .isNotEmpty()
        .extracting(ChunkPart::getMain)
        .extracting(MainPart::getTempMin, MainPart::getTempMax)
        .doesNotContain(Tuple.tuple(null, null));
  }
}
