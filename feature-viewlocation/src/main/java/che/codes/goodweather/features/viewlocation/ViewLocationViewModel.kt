package che.codes.goodweather.features.viewlocation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import che.codes.goodweather.domain.models.City
import che.codes.goodweather.domain.models.TempUnit
import che.codes.goodweather.domain.models.Weather
import che.codes.goodweather.domain.usecases.GetForecast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ViewLocationViewModel(private val getForecast: GetForecast) : ViewModel() {

    val result = MutableLiveData<Result>()
    val disposables = CompositeDisposable()

    fun loadForecast(city: City) {
        disposables.clear()
        disposables.add(getForecast.invoke(city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data ->
                    result.value = Result.Success(data.forecast, data.tempUnit)
                },
                { error ->
                    result.value = Result.Error(error)
                }
            ))
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    sealed class Result {
        data class Success(val forecast: List<Weather>, val tempUnit: TempUnit) : Result()
        data class Error(val exception: Throwable) : Result()
    }
}