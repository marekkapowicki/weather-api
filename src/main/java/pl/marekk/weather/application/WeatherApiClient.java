package pl.marekk.weather.application;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.http.MediaType;
import pl.marekk.weather.domain.RetrieveDailyTemperatureForLocationCommand;
import pl.marekk.weather.exception.Exceptions;

@Slf4j
@AllArgsConstructor
class WeatherApiClient {
  private static final HttpLoggingInterceptor.Logger httpLogger = LOG::info;
  private final OkHttpClient httpClient;
  private final HttpResponseConverter responseMapper;
  private final String weatherApiUrl;
  private final String weatherApiKey;

  static HttpLoggingInterceptor.Logger httpLogger() {
    return httpLogger;
  }

  WeatherResponse call(RetrieveDailyTemperatureForLocationCommand command) {
    LOG.info("calling the weather api with command {}", command);
    Request request = buildRequest(command);
    final Response response = tryCall(request);
    if (!response.isSuccessful()) {
      throw Exceptions.exceptionFromHttpResponse(responseMapper.takeErrorResponse(response));
    }
    return responseMapper.convertToWeatherResponse(response);
  }

  private Response tryCall(Request request) {
    try {
      return httpClient.newCall(request).execute();
    } catch (IOException e) {
      throw Exceptions.illegalState(e.getMessage());
    }
  }

  private Request buildRequest(RetrieveDailyTemperatureForLocationCommand command) {
    return new Request.Builder()
        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
        .url(buildUrl(command))
        .get()
        .build();
  }

  private HttpUrl buildUrl(RetrieveDailyTemperatureForLocationCommand command) {
    final HttpUrl httpUrl = HttpUrl.parse(weatherApiUrl);
    if (httpUrl == null) {
      throw Exceptions.illegalState("wrong weather url: " + weatherApiUrl);
    }
    return httpUrl
        .newBuilder()
        .addQueryParameter("id", command.getLocationId())
        .addQueryParameter("cnt", command.chunksNumberToFetch())
        .addQueryParameter("units", command.getTemperatureUnit().getTemperatureName())
        .addQueryParameter("appid", weatherApiKey)
        .build();
  }
}
