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
        val temp: Temperature,
        val pressure: Double,
        val humidity: Int,
        val weather: List<Weather>
    ) {

        data class Temperature(
            val day: Double,
            val min: Double,
            val max: Double,
            val night: Double,
            val eve: Double,
            val morn: Double
        )

        data class Weather(
            val main: String,
            val description: String,
            val icon: String
        )
    }
}