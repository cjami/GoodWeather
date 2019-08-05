package che.codes.goodweather.features.locationlist.di

import che.codes.goodweather.core.di.CoreComponent
import che.codes.goodweather.features.locationlist.LocationListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [CoreComponent::class], modules = [ViewModelFactoryModule::class])
interface LocationListComponent {

    fun inject(fragment: LocationListFragment)

    @Component.Builder
    interface Builder {
        fun build(): LocationListComponent
        fun coreComponent(component: CoreComponent): Builder
    }
}