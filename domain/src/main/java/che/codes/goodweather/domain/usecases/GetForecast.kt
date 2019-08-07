package che.codes.goodweather.domain.usecases

import che.codes.goodweather.domain.SETTINGS_TEMP_UNIT
import che.codes.goodweather.domain.SettingsRepository
import che.codes.goodweather.domain.TempConverter.convertKelvin
import che.codes.goodweather.domain.WeatherRepository
import che.codes.goodweather.domain.models.City
import che.codes.goodweather.domain.models.TempUnit
import che.codes.goodweather.domain.models.Weather
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class GetForecast(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) {

    fun invoke(city: City): Single<ForecastWithTempUnit> {
        return weatherRepository.getForecast(city.latitude, city.longitude).zipWith(
            settingsRepository.getBoolean(SETTINGS_TEMP_UNIT),
            BiFunction { forecast, setting ->
                val tempUnit = when (setting) {
                    true -> TempUnit.Fahrenheit
                    false -> TempUnit.Celsius
                }

                ForecastWithTempUnit(forecast.map { changeTemp(it, tempUnit) }, tempUnit)
            })
    }

    private fun changeTemp(weather: Weather, tempUnit: TempUnit): Weather {
        return Weather(
            weather.name,
            weather.description,
            convertKelvin(weather.temp, tempUnit),
            weather.day,
            weather.icon
        )
    }

    data class ForecastWithTempUnit(val forecast: List<Weather>, val tempUnit: TempUnit)
}