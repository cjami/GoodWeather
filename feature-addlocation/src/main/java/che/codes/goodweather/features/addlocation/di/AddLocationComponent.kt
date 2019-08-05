package che.codes.goodweather.features.addlocation.di

import che.codes.goodweather.core.di.CoreComponent
import che.codes.goodweather.features.addlocation.AddLocationFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [CoreComponent::class], modules = [AddLocationModule::class])
interface AddLocationComponent {

    fun inject(fragment: AddLocationFragment)

    @Component.Builder
    interface Builder {
        fun build(): AddLocationComponent
        fun coreComponent(component: CoreComponent): Builder
    }
}