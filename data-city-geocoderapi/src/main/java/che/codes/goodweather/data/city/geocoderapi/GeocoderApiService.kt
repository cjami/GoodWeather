package che.codes.goodweather.data.city.geocoderapi

import che.codes.goodweather.data.city.geocoderapi.models.GeocodeResultPayload
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocoderApiService {

    @GET("json")
    fun geocode(@Query("address") address: String, @Query("key") apiKey: String) : Single<GeocodeResultPayload>
}