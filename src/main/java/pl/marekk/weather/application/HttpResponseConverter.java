package pl.marekk.weather.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import okhttp3.Response;
import pl.marekk.weather.exception.Exceptions;

@AllArgsConstructor
class HttpResponseConverter {
  private final ObjectMapper objectMapper;

  WeatherResponse convertToWeatherResponse(Response response) {
    return responseAsType(response, WeatherResponse.class);
  }

  WeatherErrorResponse takeErrorResponse(Response response) {
    return responseAsType(response, WeatherErrorResponse.class);
  }

  private <T> T responseAsType(Response response, Class<T> responseClass) {
    String responseAsString = Responses.bodyAsString(response);
    try {
      return objectMapper.readValue(responseAsString, responseClass);
    } catch (JsonProcessingException e) {
      throw Exceptions.illegalState("error during mapping response to class " + e.getMessage());
    }
  }
}
