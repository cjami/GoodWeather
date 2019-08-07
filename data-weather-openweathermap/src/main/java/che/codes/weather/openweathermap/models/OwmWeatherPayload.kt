package che.codes.weather.openweathermap.models

data class OwmWeatherPayload(
    val city: City,
    val list: List<Result>
) {
    data class City(
        val timezone: Int
    )

    data class Result(
        val dt: Long,
        val main: Main,
        val weather: List<Weather>
    ) {

        data class Main(
            val temp: Double
        )

        data class Weather(
            val id: Int,
            val main: String,
            val description: String,
            val icon: String
        )
    }
}