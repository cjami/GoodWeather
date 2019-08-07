package che.codes.goodweather.features.viewlocation.di

import che.codes.goodweather.domain.usecases.GetForecast
import che.codes.goodweather.features.viewlocation.ViewLocationViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewLocationModule {

    @Provides
    fun provideViewLocationViewModelFactory(getForecast: GetForecast): ViewLocationViewModelFactory {
        return ViewLocationViewModelFactory(getForecast)
    }
}