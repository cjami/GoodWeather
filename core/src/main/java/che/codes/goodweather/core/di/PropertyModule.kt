package che.codes.goodweather.core.di

import che.codes.goodweather.core.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Named

private const val GEOCODER_API_URL = "https://maps.googleapis.com/maps/api/geocode/"
private const val OPENWEATHERMAP_API_URL = "https://api.openweathermap.org/data/2.5/forecast/"

@Module
class PropertyModule {

    @Provides
    @Named("geocoderapi.url")
    fun provideGeocoderApiUrl(): String {
        return GEOCODER_API_URL
    }

    @Provides
    @Named("geocoderapi.key")
    fun provideGeocoderApiKey(): String {
        return BuildConfig.GEOCODER_API_KEY
    }

    @Provides
    @Named("openweathermap.url")
    fun provideOpenWeatherMapUrl(): String {
        return OPENWEATHERMAP_API_URL
    }

    @Provides
    @Named("openweathermap.id")
    fun provideOpenWeatherMapAppId(): String {
        return BuildConfig.OPENWEATHERMAP_APP_ID
    }
}