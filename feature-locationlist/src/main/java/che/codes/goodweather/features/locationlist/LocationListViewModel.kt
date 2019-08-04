package che.codes.goodweather.features.locationlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import che.codes.goodweather.domain.models.City
import che.codes.goodweather.domain.usecases.AddCity
import che.codes.goodweather.domain.usecases.LoadCities
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LocationListViewModel(private val loadCities: LoadCities, private val addCity: AddCity) : ViewModel() {

    val result = MutableLiveData<Result>()
    val disposables = CompositeDisposable()

    fun refresh() {
        disposables.add(loadCities.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data ->
                    result.value = Result.Success(data)
                },
                { error ->
                    result.value = Result.Error(error)
                }
            ))
    }

    fun add(city: City) {
        addCity.invoke(city)
        refresh()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    sealed class Result {
        data class Success(val cities: List<City>) : Result()
        data class Error(val exception: Throwable) : Result()
    }
}