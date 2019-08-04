package che.codes.goodweather.data.weather

import che.codes.goodweather.domain.WeatherRepository
import che.codes.goodweather.domain.models.Weather
import io.reactivex.Single

class DefaultWeatherRepository(private val source: WeatherDataSource) : WeatherRepository {

    override fun getForecast(latitude: Double, longitude: Double, days: Int): Single<List<Weather>> {
        return source.getForecast(latitude, longitude, days)
    }
}