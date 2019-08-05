package che.codes.goodweather.features.addlocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import che.codes.goodweather.domain.usecases.AddCity
import che.codes.goodweather.domain.usecases.GeocodeCity

class AddLocationViewModelFactory(
    private val geocodeCity: GeocodeCity,
    private val addCity: AddCity
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddLocationViewModel::class.java) -> {
                AddLocationViewModel(geocodeCity, addCity) as T
            }
            else -> throw IllegalArgumentException(
                "${modelClass.simpleName} is an unknown view model type"
            )
        }
    }
}