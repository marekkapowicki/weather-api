package pl.marekk.weather.application;

import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
public class WeatherErrorResponse {
  int cod;
  String message;

  static WeatherErrorResponse unknownErrorResponse() {
    return new WeatherErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(), "unknown weather api exception");
  }
}
