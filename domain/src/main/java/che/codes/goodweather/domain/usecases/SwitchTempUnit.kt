package che.codes.goodweather.domain.usecases

import che.codes.goodweather.domain.SettingsRepository
import che.codes.goodweather.domain.SETTINGS_TEMP_UNIT
import che.codes.goodweather.domain.models.TempUnit

class SwitchTempUnit(private val repository: SettingsRepository) {

    fun invoke(unit: TempUnit) {
        repository.put(SETTINGS_TEMP_UNIT, unit == TempUnit.Fahrenheit)
    }
}