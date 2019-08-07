package che.codes.goodweather.features.settings.di

import che.codes.goodweather.domain.usecases.GetTempUnit
import che.codes.goodweather.domain.usecases.SwitchTempUnit
import che.codes.goodweather.features.settings.SettingsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SettingsModule {

    @Provides
    fun provideSettingsViewModelFactory(getTempUnit: GetTempUnit, switchTempUnit: SwitchTempUnit): SettingsViewModelFactory {
        return SettingsViewModelFactory(getTempUnit, switchTempUnit)
    }
}