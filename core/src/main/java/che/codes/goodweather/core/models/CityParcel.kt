package che.codes.goodweather.core.models

import android.os.Parcelable
import che.codes.goodweather.domain.models.City
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CityParcel(val name: String, val country: CountryParcel, val latitude: Double, val longitude: Double) : Parcelable {

    @Parcelize
    data class CountryParcel(val longName: String, val shortName: String) : Parcelable

    fun toCity(): City {
        return City(name, City.Country(country.longName, country.shortName), latitude, longitude)
    }

    companion object {
        fun fromCity(city: City): CityParcel {
            return CityParcel(
                city.name,
                CountryParcel(city.country.longName, city.country.shortName),
                city.latitude,
                city.longitude
            )
        }
    }
}