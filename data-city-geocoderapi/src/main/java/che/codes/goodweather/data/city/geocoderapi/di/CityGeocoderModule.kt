package che.codes.goodweather.data.city.geocoderapi.di

import che.codes.goodweather.data.city.CityGeocoder
import che.codes.goodweather.data.city.geocoderapi.GeocoderApiCityGeocoder
import che.codes.goodweather.data.city.geocoderapi.GeocoderApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class CityGeocoderModule {

    @Provides
    fun provideCityGeocoder(
        okHttpClient: OkHttpClient,
        @Named("geocoderapi.url") baseUrl: String,
        @Named("geocoderapi.key") apiKey: String
    ): CityGeocoder {
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return GeocoderApiCityGeocoder(retrofit.create(GeocoderApiService::class.java), apiKey)
    }
}