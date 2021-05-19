package pl.marekk.weather.application;

import java.io.IOException;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.Response;
import pl.marekk.weather.exception.Exceptions;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Responses {

  static String bodyAsString(Response response) {
    try {
      return Objects.requireNonNull(response.body()).string();
    } catch (RuntimeException | IOException e) {
      throw Exceptions.illegalState("error during parsing http request " + e.getMessage());
    }
  }
}
