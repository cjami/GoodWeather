package che.codes.goodweather.core.di

import android.content.Context
import che.codes.goodweather.core.BuildConfig
import che.codes.goodweather.data.city.geocoderapi.di.CityGeocoderModule
import che.codes.goodweather.data.city.sharedprefs.di.CityStorageModule
import che.codes.goodweather.data.di.DataModule
import che.codes.goodweather.data.settings.sharedprefs.di.SettingsStorageModule
import che.codes.goodweather.domain.CityRepository
import che.codes.goodweather.domain.SettingsRepository
import che.codes.goodweather.domain.WeatherRepository
import che.codes.goodweather.domain.usecases.*
import che.codes.weather.openweathermap.di.WeatherDataSourceModule
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Component(
    modules = [
        AppContextModule::class,
        NetworkModule::class,
        DataModule::class,
        CityGeocoderModule::class,
        CityStorageModule::class,
        WeatherDataSourceModule::class,
        SettingsStorageModule::class,
        UseCaseModule::class,
        PropertyModule::class
    ]
)
interface CoreComponent {

    @Singleton
    fun provideOkHttpClient(): OkHttpClient

    fun provideAddCity(): AddCity
    fun provideGeocodeCity(): GeocodeCity
    fun provideGetFourDayForecast(): GetFourDayForecast
    fun provideGetTempUnit(): GetTempUnit
    fun provideLoadCities(): LoadCities
    fun provideSwitchTempUnit(): SwitchTempUnit

    @Singleton
    fun provideAppContext(): Context

    interface Provider {
        fun provideCoreComponent(): CoreComponent
        fun setCoreComponent(coreComponent: CoreComponent)
    }

    companion object {
        fun getInstance(applicationContext: Context): CoreComponent {
            return (applicationContext as Provider).provideCoreComponent()
        }
    }
}