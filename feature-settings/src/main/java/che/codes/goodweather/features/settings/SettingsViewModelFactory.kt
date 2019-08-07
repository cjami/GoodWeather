package che.codes.goodweather.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import che.codes.goodweather.domain.usecases.GetTempUnit
import che.codes.goodweather.domain.usecases.SwitchTempUnit

class SettingsViewModelFactory(private val getTempUnit: GetTempUnit, private val switchTempUnit: SwitchTempUnit) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(getTempUnit, switchTempUnit) as T
            }
            else -> throw IllegalArgumentException(
                "${modelClass.simpleName} is an unknown view model type"
            )
        }
    }
}