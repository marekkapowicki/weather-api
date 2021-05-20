package pl.marekk.weather.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

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
    return new ResponseStatusException(INTERNAL_SERVER_ERROR, message);
  }

  //TODO add counter
  public static RuntimeException requestsLimitExceeded() {
    final String message = "requests limit exceeded";
    LOG.warn(message);
    return new ResponseStatusException(TOO_MANY_REQUESTS, message);
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
      return INTERNAL_SERVER_ERROR;
    }
  }
}
