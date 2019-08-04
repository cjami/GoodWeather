package che.codes.goodweather.domain.usecases

import che.codes.goodweather.domain.SETTINGS_TEMP_UNIT
import che.codes.goodweather.domain.SettingsRepository
import che.codes.goodweather.domain.models.TempUnit
import io.reactivex.Single

class GetTempUnit(private val repository: SettingsRepository) {

    fun invoke(): Single<TempUnit> {
        return repository.getBoolean(SETTINGS_TEMP_UNIT).map { value ->
            when (value) {
                true -> TempUnit.Fahrenheit
                false -> TempUnit.Celsius
            }
        }
    }
}