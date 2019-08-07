package che.codes.goodweather.features.viewlocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import che.codes.goodweather.domain.usecases.GetForecast

class ViewLocationViewModelFactory(private val getForecast: GetForecast) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ViewLocationViewModel::class.java) -> {
                ViewLocationViewModel(getForecast) as T
            }
            else -> throw IllegalArgumentException(
                "${modelClass.simpleName} is an unknown view model type"
            )
        }
    }
}