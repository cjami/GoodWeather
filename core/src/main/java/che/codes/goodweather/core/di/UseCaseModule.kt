package che.codes.goodweather.core.di

import che.codes.goodweather.domain.CityRepository
import che.codes.goodweather.domain.usecases.LoadCities
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun provideLoadCities(repository: CityRepository): LoadCities{
        return LoadCities(repository)
    }
}