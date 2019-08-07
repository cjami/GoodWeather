package che.codes.weather.openweathermap

import che.codes.weather.openweathermap.models.OwmWeatherPayload
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OwmApiService {

    @GET("forecast")
    fun getForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appId: String
    ): Single<OwmWeatherPayload>
}