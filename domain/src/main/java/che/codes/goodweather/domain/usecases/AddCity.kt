package che.codes.goodweather.domain.usecases

import che.codes.goodweather.domain.CityRepository
import che.codes.goodweather.domain.models.City

class AddCity(private val repository: CityRepository) {

    fun invoke(city: City){
        repository.store(city)
    }
}