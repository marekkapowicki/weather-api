package pl.marekk.weather.application;

import static pl.marekk.weather.domain.RetrieveDailyTemperatureForLocationCommand.retrieveDailyTemperatureCommand;

import java.time.LocalDateTime;
import pl.marekk.weather.domain.RetrieveDailyTemperatureForLocationCommand;
import pl.marekk.weather.domain.TemperatureUnit;

class TemperatureDailyCommands {
  static final RetrieveDailyTemperatureForLocationCommand sampleDailyCommand =
      retrieveDailyTemperatureCommand(LocalDateTime.now(), "858787", TemperatureUnit.CELCIUS);
}
