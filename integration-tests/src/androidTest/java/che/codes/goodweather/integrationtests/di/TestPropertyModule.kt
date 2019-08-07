package che.codes.goodweather.integrationtests.di

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class TestPropertyModule(val geocoderApiUrl: String = "", val openweatherApiUrl: String = "") {

    @Provides
    @Named("geocoderapi.url")
    fun provideGeocoderApiUrl(): String {
        return geocoderApiUrl
    }

    @Provides
    @Named("geocoderapi.key")
    fun provideGeocoderApiKey(): String {
        return ""
    }

    @Provides
    @Named("openweathermap.url")
    fun provideOpenWeatherMapUrl(): String {
        return openweatherApiUrl
    }

    @Provides
    @Named("openweathermap.id")
    fun provideOpenWeatherMapAppId(): String {
        return ""
    }

}