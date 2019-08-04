package che.codes.weather.openweathermap

import che.codes.goodweather.data.weather.WeatherDataSource
import che.codes.goodweather.domain.models.Weather
import che.codes.weather.openweathermap.models.OwmWeatherPayload
import io.reactivex.Single
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset

class OwmWeatherDataSource(private val service: OwmApiService, private val appId: String) : WeatherDataSource {

    override fun getForecast(latitude: Double, longitude: Double, days: Int): Single<List<Weather>> {
        return service.getForecast(latitude, longitude, days, appId).map { payload -> map(payload) }
    }

    private fun map(payload: OwmWeatherPayload): List<Weather> {
        return payload.list.map {
            val weatherData = it.weather[0]

            Weather(
                weatherData.main,
                weatherData.description,
                it.temp.day,
                dayOfWeek(it.dt, payload.city.timezone),
                weatherData.icon
            )
        }
    }

    private fun dayOfWeek(dt: Long, timezone: Int): String {
        return Instant.ofEpochSecond(dt).atOffset(ZoneOffset.ofTotalSeconds(timezone)).dayOfWeek.toString()
    }
}