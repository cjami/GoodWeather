package che.codes.goodweather.features.addlocation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import che.codes.goodweather.domain.models.City
import che.codes.goodweather.domain.usecases.AddCity
import che.codes.goodweather.domain.usecases.GeocodeCity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddLocationViewModel(private val geocodeCity: GeocodeCity, private val addCity: AddCity) : ViewModel() {

    val result = MutableLiveData<Result>()
    val disposables = CompositeDisposable()

    fun geocode(cityName: String) {
        disposables.add(geocodeCity.invoke(cityName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { result.value = Result.Processing }
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
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    sealed class Result {
        object Processing : Result()
        data class Success(val city: City) : Result()
        data class Error(val exception: Throwable) : Result()
    }
}