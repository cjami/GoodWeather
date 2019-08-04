package che.codes.goodweather.domain.usecases

import che.codes.goodweather.domain.WeatherRepository
import che.codes.goodweather.domain.models.City
import che.codes.goodweather.domain.models.Weather
import io.reactivex.Single

private const val FOUR_DAYS = 4

class GetFourDayForecast(private val repository: WeatherRepository) {

    fun invoke(city: City): Single<List<Weather>> {
        return repository.getForecast(city.latitude, city.longitude, FOUR_DAYS)
    }
}