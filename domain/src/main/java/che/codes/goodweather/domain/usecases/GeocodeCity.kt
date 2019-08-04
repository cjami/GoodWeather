package che.codes.goodweather.domain.usecases

import che.codes.goodweather.domain.CityRepository
import che.codes.goodweather.domain.models.City
import io.reactivex.Single

class GeocodeCity(private val repository: CityRepository) {

    fun invoke(cityName: String): Single<City> {
        return repository.geocode(cityName)
    }
}