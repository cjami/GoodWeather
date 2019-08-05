package che.codes.goodweather.core.di

import che.codes.goodweather.domain.CityRepository
import che.codes.goodweather.domain.SettingsRepository
import che.codes.goodweather.domain.WeatherRepository
import che.codes.goodweather.domain.usecases.*
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun provideAddCity(repository: CityRepository): AddCity {
        return AddCity(repository)
    }

    @Provides
    fun provideGeocodeCity(repository: CityRepository): GeocodeCity {
        return GeocodeCity(repository)
    }

    @Provides
    fun provideGetFourDayForecast(repository: WeatherRepository): GetFourDayForecast {
        return GetFourDayForecast(repository)
    }

    @Provides
    fun provideGetTempUnit(repository: SettingsRepository): GetTempUnit {
        return GetTempUnit(repository)
    }

    @Provides
    fun provideLoadCities(repository: CityRepository): LoadCities {
        return LoadCities(repository)
    }

    @Provides
    fun provideSwitchTempUnit(repository: SettingsRepository): SwitchTempUnit {
        return SwitchTempUnit(repository)
    }
}