package pl.marekk.weather.application;

import static pl.marekk.weather.domain.RetrieveDailyTemperatureForLocationCommand.retrieveTomorrowTemperatureCommand;

import java.time.LocalDateTime;
import pl.marekk.weather.domain.RetrieveDailyTemperatureForLocationCommand;
import pl.marekk.weather.domain.TemperatureUnit;

class TemperatureDailyCommands {
  static final RetrieveDailyTemperatureForLocationCommand sampleDailyCommand =
      retrieveTomorrowTemperatureCommand(LocalDateTime.now(), "858787", TemperatureUnit.CELCIUS);
}
