package che.codes.goodweather.domain

import che.codes.goodweather.domain.models.Weather
import io.reactivex.Single

interface WeatherRepository {

    fun getForecast(latitude: Double, longitude: Double): Single<List<Weather>>
}