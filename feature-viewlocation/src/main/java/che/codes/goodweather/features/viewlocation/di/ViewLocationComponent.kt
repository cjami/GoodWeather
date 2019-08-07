package che.codes.goodweather.features.viewlocation.di

import che.codes.goodweather.core.di.CoreComponent
import che.codes.goodweather.features.viewlocation.ViewLocationFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [CoreComponent::class], modules = [ViewLocationModule::class])
interface ViewLocationComponent {

    fun inject(fragment: ViewLocationFragment)

    @Component.Builder
    interface Builder {
        fun build(): ViewLocationComponent
        fun coreComponent(component: CoreComponent): Builder
    }
}