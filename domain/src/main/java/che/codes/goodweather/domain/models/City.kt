package che.codes.goodweather.domain.models

data class City(val name: String, val country: Country, val latitude: Double, val longitude: Double) {

    data class Country(val longName: String, val shortName: String)
}