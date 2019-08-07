package che.codes.goodweather.features.settings.di

import che.codes.goodweather.core.di.CoreComponent
import che.codes.goodweather.features.settings.SettingsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [CoreComponent::class], modules = [SettingsModule::class])
interface SettingsComponent {

    fun inject(fragment: SettingsFragment)

    @Component.Builder
    interface Builder {
        fun build(): SettingsComponent
        fun coreComponent(component: CoreComponent): Builder
    }
}