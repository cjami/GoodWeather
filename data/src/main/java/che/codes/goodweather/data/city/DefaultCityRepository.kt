package che.codes.goodweather.data.city

import che.codes.goodweather.domain.CityRepository
import che.codes.goodweather.domain.models.City
import io.reactivex.Single

class DefaultCityRepository(private val geocoder: CityGeocoder, private val storage: CityStorage) : CityRepository {

    override fun geocode(cityName: String): Single<City> {
        return geocoder.geocode(cityName)
    }

    override fun store(city: City) {
        storage.store(city)
    }

    override fun retrieve(): Single<List<City>> {
        return storage.retrieve()
    }
}