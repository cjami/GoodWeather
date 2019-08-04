package che.codes.goodweather.domain.usecases

import che.codes.goodweather.domain.CityRepository
import che.codes.goodweather.domain.models.City
import io.reactivex.Single

class LoadCities(private val repository: CityRepository) {

    fun invoke(): Single<List<City>> {
        return repository.retrieve()
    }
}