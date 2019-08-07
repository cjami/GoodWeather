package che.codes.goodweather.data.weather

import che.codes.goodweather.domain.models.Weather
import io.reactivex.Single

interface WeatherDataSource {

    fun getForecast(latitude: Double, longitude: Double) : Single<List<Weather>>
}