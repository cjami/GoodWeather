package che.codes.goodweather.domain

import che.codes.goodweather.domain.models.City
import io.reactivex.Single

interface CityRepository {

    fun geocode(cityName: String): Single<City>

    fun store(city: City)

    fun retrieve(): Single<List<City>>
}