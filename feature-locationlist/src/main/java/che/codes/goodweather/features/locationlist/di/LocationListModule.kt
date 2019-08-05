package che.codes.goodweather.features.locationlist.di

import che.codes.goodweather.domain.usecases.LoadCities
import che.codes.goodweather.features.locationlist.LocationListViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class LocationListModule {

    @Provides
    fun provideLocationListViewModelFactory(loadCities: LoadCities): LocationListViewModelFactory {
        return LocationListViewModelFactory(loadCities)
    }
}