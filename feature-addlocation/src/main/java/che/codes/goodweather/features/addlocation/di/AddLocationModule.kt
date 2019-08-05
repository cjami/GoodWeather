package che.codes.goodweather.features.addlocation.di

import che.codes.goodweather.domain.usecases.AddCity
import che.codes.goodweather.domain.usecases.GeocodeCity
import che.codes.goodweather.features.addlocation.AddLocationViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class AddLocationModule {

    @Provides
    fun provideAddLocationViewModelFactory(geocodeCity: GeocodeCity, addCity: AddCity): AddLocationViewModelFactory {
        return AddLocationViewModelFactory(geocodeCity, addCity)
    }
}