package che.codes.goodweather.data.city.geocoderapi

import che.codes.goodweather.data.city.CityGeocoder
import che.codes.goodweather.data.city.geocoderapi.models.GeocodeResultPayload
import che.codes.goodweather.domain.models.City
import io.reactivex.Single

class GeocoderApiCityGeocoder(private val service: GeocoderApiService, private val apiKey: String) : CityGeocoder {

    override fun geocode(name: String): Single<City> {
        return service.geocode(name, apiKey).map { payload -> map(payload) }
    }

    private fun map(payload: GeocodeResultPayload): City {
        require(payload.results.isNotEmpty()) { "No geocode result found" }

        val result = payload.results[0]
        val city = result.addressComponents.find { it.types.contains("locality") }
            ?: result.addressComponents.find { it.types.contains("administrative_area_level_1") } // For Japan
        val country = result.addressComponents.find { it.types.contains("country") }

        check(city != null) { "No valid city found" }
        check(country != null) { "No valid country found" }

        return City(
            city.longName,
            City.Country(country.longName, country.shortName),
            result.geometry.location.lat,
            result.geometry.location.lng
        )
    }
}