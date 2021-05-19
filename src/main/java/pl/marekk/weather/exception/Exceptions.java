package pl.marekk.weather.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.marekk.weather.application.WeatherErrorResponse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class Exceptions {
  public static RuntimeException illegalState(String message) {
    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }

  public static RuntimeException exceptionFromHttpResponse(WeatherErrorResponse errorResponse) {
    final String responseError = errorResponse.getMessage();
    final HttpStatus statusCode = httpStatus(errorResponse.getCod());
    return new ResponseStatusException(statusCode, responseError);
  }

  private static HttpStatus httpStatus(int statusCode) {
    try {
      return HttpStatus.valueOf(statusCode);
    } catch (IllegalArgumentException e) {
      LOG.warn("error during mapping http status {}: {}", statusCode, e.getMessage());
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
  }
}
