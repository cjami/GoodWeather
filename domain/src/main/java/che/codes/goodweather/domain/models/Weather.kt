package che.codes.goodweather.domain.models

data class Weather(
    val name: String,
    val description: String,
    val temp: Double,
    val day: String,
    val icon: String
)