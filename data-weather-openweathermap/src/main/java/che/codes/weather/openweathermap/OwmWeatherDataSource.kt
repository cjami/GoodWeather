package che.codes.weather.openweathermap

import che.codes.goodweather.data.weather.WeatherDataSource
import che.codes.goodweather.domain.models.Weather
import che.codes.weather.openweathermap.models.OwmWeatherPayload
import io.reactivex.Single
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset

private const val NUMBER_OF_DAYS = 4

class OwmWeatherDataSource(private val service: OwmApiService, private val appId: String) : WeatherDataSource {

    override fun getForecast(latitude: Double, longitude: Double): Single<List<Weather>> {
        return service.getForecast(latitude, longitude, appId).map { payload -> map(payload) }
    }

    /**
     * Maps payload and converts hourly forecast to daily forecast
     *
     * This is done by taking an average of the daily temperatures and finding the most common weather type
     *
     * Only uses weather during daytime (6AM - 8PM) to avoid night time weather conditions
     */
    private fun map(payload: OwmWeatherPayload): List<Weather> {
        val weatherList = mutableListOf<Weather>()
        val timezone = payload.city.timezone
        val startDateTime: OffsetDateTime = dateTime(payload.list[0].dt, timezone)
        val lastTime = payload.list[payload.list.size - 1].dt
        var startBoundary: Long = 0
        var endBoundary: Long

        var dayIndex = 0
        while (weatherList.size < NUMBER_OF_DAYS && startBoundary < lastTime) {
            // e.g. MON 06:00 to MON 20:00 to find MON weather
            startDateTime.plusDays(dayIndex.toLong()).let {
                startBoundary = dayStart(it).toEpochSecond()
                endBoundary = dayEnd(it).toEpochSecond()
            }

            val dayResultItems = payload.list.filter { it.dt in startBoundary..endBoundary }

            val avgTemp = dayResultItems.map { it.main.temp }.average()

            val weatherCountMap = dayResultItems.groupingBy { it.weather[0] }.eachCount()

            // Most common weather for the day
            weatherCountMap.maxBy { it.value }?.key?.let {
                weatherList.add(
                    Weather(it.main, it.description, avgTemp, dayOfWeek(startBoundary, timezone), it.icon)
                )
            }

            // Search next day
            dayIndex++
        }

        return weatherList
    }

    private fun dateTime(dt: Long, timezone: Int): OffsetDateTime {
        return Instant.ofEpochSecond(dt).atOffset(ZoneOffset.ofTotalSeconds(timezone))
    }

    private fun dayOfWeek(dt: Long, timezone: Int): String {
        return dateTime(dt, timezone).dayOfWeek.toString()
    }

    private fun dayStart(dateTime: OffsetDateTime): OffsetDateTime {
        return dateTime.withHour(6).withMinute(0).withSecond(0).withNano(0) // 6 AM
    }

    private fun dayEnd(dateTime: OffsetDateTime): OffsetDateTime {
        return dateTime.withHour(20).withMinute(0).withSecond(0).withNano(0) // 8 PM
    }

}