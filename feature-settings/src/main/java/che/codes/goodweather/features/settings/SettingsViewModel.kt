package che.codes.goodweather.features.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import che.codes.goodweather.domain.SETTINGS_TEMP_UNIT
import che.codes.goodweather.domain.models.TempUnit
import che.codes.goodweather.domain.usecases.GetTempUnit
import che.codes.goodweather.domain.usecases.SwitchTempUnit
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * TODO: This should be generalised to better work with multiple settings
 */
class SettingsViewModel(private val getTempUnit: GetTempUnit, private val switchTempUnit: SwitchTempUnit) :
    ViewModel() {

    val result = MutableLiveData<Result>()
    val disposables = CompositeDisposable()

    fun refresh() {
        disposables.add(getTempUnit.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data ->
                    result.value = Result.Success(mapOf(Pair(SETTINGS_TEMP_UNIT, data)))
                },
                { error ->
                    result.value = Result.Error(error)
                }
            ))
    }

    fun set(settingKey: String, value: Boolean) {
        when (settingKey) {
            SETTINGS_TEMP_UNIT -> {
                val tempUnit = when (value) {
                    true -> TempUnit.Fahrenheit
                    false -> TempUnit.Celsius
                }
                switchTempUnit.invoke(tempUnit)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    sealed class Result {
        data class Success(val settings: Map<String, Any>) : Result()
        data class Error(val exception: Throwable) : Result()
    }
}