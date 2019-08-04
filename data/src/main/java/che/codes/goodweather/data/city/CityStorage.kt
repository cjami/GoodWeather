package che.codes.goodweather.data.city

import che.codes.goodweather.domain.models.City
import io.reactivex.Single

interface CityStorage {

    fun store(city: City)

    fun retrieve(): Single<List<City>>
}