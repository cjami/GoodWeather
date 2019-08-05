package che.codes.goodweather.features.locationlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import che.codes.goodweather.domain.usecases.AddCity
import che.codes.goodweather.domain.usecases.LoadCities

class LocationListViewModelFactory(private val loadCities: LoadCities) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LocationListViewModel::class.java) -> {
                LocationListViewModel(loadCities) as T
            }
            else -> throw IllegalArgumentException(
                "${modelClass.simpleName} is an unknown view model type"
            )
        }
    }
}