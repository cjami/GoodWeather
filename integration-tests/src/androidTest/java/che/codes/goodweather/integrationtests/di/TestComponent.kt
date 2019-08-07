package che.codes.goodweather.integrationtests.di

import che.codes.goodweather.core.di.AppContextModule
import che.codes.goodweather.core.di.CoreComponent
import che.codes.goodweather.core.di.NetworkModule
import che.codes.goodweather.core.di.UseCaseModule
import che.codes.goodweather.data.city.geocoderapi.di.CityGeocoderModule
import che.codes.goodweather.data.city.sharedprefs.di.CityStorageModule
import che.codes.goodweather.data.di.DataModule
import che.codes.goodweather.data.settings.sharedprefs.di.SettingsStorageModule
import che.codes.weather.openweathermap.di.WeatherDataSourceModule
import dagger.Component
import javax.inject.Singleton

@Singleton
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
        TestPropertyModule::class
    ]
)
interface TestCoreComponent : CoreComponent