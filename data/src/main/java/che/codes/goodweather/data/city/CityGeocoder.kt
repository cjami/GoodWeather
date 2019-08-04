package che.codes.goodweather.data.city

import che.codes.goodweather.domain.models.City
import io.reactivex.Single

interface CityGeocoder {

    fun geocode(name: String) : Single<City>
}