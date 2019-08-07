package che.codes.goodweather.features.viewlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import che.codes.goodweather.core.di.CoreComponent
import che.codes.goodweather.core.models.CityParcel
import che.codes.goodweather.core.ui.BaseFragment
import che.codes.goodweather.domain.models.TempUnit
import che.codes.goodweather.domain.models.Weather
import che.codes.goodweather.features.viewlocation.ViewLocationViewModel.Result
import che.codes.goodweather.features.viewlocation.di.DaggerViewLocationComponent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_view_location.*
import kotlinx.android.synthetic.main.item_day_forecast.view.*
import javax.inject.Inject
import kotlin.math.roundToInt

const val ARG_CITY = "ARG_CITY"
private const val WEATHER_IMAGE_URL = "https://openweathermap.org/img/wn/"

class ViewLocationFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewLocationViewModelFactory

    private lateinit var viewModel: ViewLocationViewModel

    private val city by lazy {
        val parcel = arguments?.getParcelable<CityParcel>(ARG_CITY)
        parcel?.toCity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerViewLocationComponent.builder()
            .coreComponent(CoreComponent.getInstance(activity!!.applicationContext))
            .build().inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ViewLocationViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.result.observe(this, Observer<Result> { handleResult(it) })
        expandToolbar()
    }

    override fun onResume() {
        super.onResume()

        city?.let {
            viewModel.loadForecast(it)
            setActionBarTitle(getString(R.string.format_location, it.name, it.country.shortName))
        }
    }

    private fun handleResult(result: Result) {
        when (result) {
            is Result.Success -> {
                val forecast = result.forecast
                renderDayOneWeather(forecast[0], result.tempUnit)
                renderWeather(day_two, forecast[1], result.tempUnit)
                renderWeather(day_three, forecast[2], result.tempUnit)
                renderWeather(day_four, forecast[3], result.tempUnit)
            }
            is Result.Error -> {
                Toast.makeText(context, result.exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun renderDayOneWeather(weather: Weather, tempUnit: TempUnit) {
        text_day.text = weather.day.toLowerCase().capitalize()
        loadIcon(image_weather, weather.icon)
        text_temp.text =
            getString(R.string.format_temperature, weather.temp.roundToInt().toString(), getTempUnitString(tempUnit))
    }

    private fun renderWeather(view: View, weather: Weather, tempUnit: TempUnit) {
        view.text_day.text = weather.day.substring(0..2).toUpperCase()
        loadIcon(view.image_weather, weather.icon)
        view.text_temp.text =
            getString(R.string.format_temperature, weather.temp.roundToInt().toString(), getTempUnitString(tempUnit))
    }

    private fun getTempUnitString(tempUnit: TempUnit): String {
        return when (tempUnit) {
            TempUnit.Fahrenheit -> getString(R.string.fahrenheit)
            TempUnit.Celsius -> getString(R.string.celsius)
        }
    }

    private fun loadIcon(imageView: ImageView, iconCode: String) {
        Picasso.get().load("$WEATHER_IMAGE_URL$iconCode@2x.png").into(imageView)
    }
}