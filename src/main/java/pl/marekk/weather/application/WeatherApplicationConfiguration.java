package pl.marekk.weather.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = WeatherApiProperties.class)
class WeatherApplicationConfiguration {
  private static HttpLoggingInterceptor httpLoggingInterceptor(
      HttpLoggingInterceptor.Logger logger) {
    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(logger);
    httpLoggingInterceptor.setLevel(Level.BASIC);
    return httpLoggingInterceptor;
  }

  @Bean
  HttpResponseConverter responseMapper(ObjectMapper objectMapper) {
    return new HttpResponseConverter(objectMapper);
  }

  @Bean
  OkHttpClient weatherClient() {
    return new Builder()
        .readTimeout(Duration.ZERO)
        .addInterceptor(httpLoggingInterceptor(WeatherApiClient.httpLogger()))
        .build();
  }

  @Bean
  WeatherApiClient weatherApiClient(
      WeatherApiProperties apiProperties,
      OkHttpClient weatherClient,
      HttpResponseConverter responseMapper) {
    return new WeatherApiClient(
        weatherClient, responseMapper, apiProperties.getUrl(), apiProperties.getKey());
  }

  @Bean
  TemperatureForecastFactory temperatureForecastFactory(WeatherApiClient weatherApiClient) {
    return new TemperatureForecastFactory(weatherApiClient);
  }

  @Bean
  TemperatureForecastFacade temperatureForecastFacade(
      TemperatureForecastFactory temperatureForecastFactory) {
    return new DefaultTemperatureForecastFacade(temperatureForecastFactory);
  }
}
