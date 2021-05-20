package pl.marekk.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.marekk.throttling.EnableThrottling;

@SpringBootApplication
@EnableThrottling
public class WeatherApplication {

  public static void main(String[] args) {
    SpringApplication.run(WeatherApplication.class, args);
  }
}
