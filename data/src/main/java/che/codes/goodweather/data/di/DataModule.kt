package che.codes.goodweather.data.di

import che.codes.goodweather.data.city.CityGeocoder
import che.codes.goodweather.data.city.CityStorage
import che.codes.goodweather.data.city.DefaultCityRepository
import che.codes.goodweather.data.settings.DefaultSettingsRepository
import che.codes.goodweather.data.settings.SettingsStorage
import che.codes.goodweather.data.weather.DefaultWeatherRepository
import che.codes.goodweather.data.weather.WeatherDataSource
import che.codes.goodweather.domain.CityRepository
import che.codes.goodweather.domain.SettingsRepository
import che.codes.goodweather.domain.WeatherRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideWeatherRepository(source: WeatherDataSource): WeatherRepository {
        return DefaultWeatherRepository(source)
    }

    @Provides
    fun provideCityRepository(geocoder: CityGeocoder, storage: CityStorage): CityRepository {
        return DefaultCityRepository(geocoder, storage)
    }

    @Provides
    fun provideSettingsRepository(storage: SettingsStorage): SettingsRepository {
        return DefaultSettingsRepository(storage)
    }
}