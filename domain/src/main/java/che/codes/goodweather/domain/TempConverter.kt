package che.codes.goodweather.domain

import che.codes.goodweather.domain.models.TempUnit
import java.math.RoundingMode

object TempConverter {

    fun convertKelvin(kelvin: Double, tempUnit: TempUnit): Double {
        val raw =  when (tempUnit) {
            TempUnit.Fahrenheit -> convertKelvinToFahrenheit(kelvin)
            TempUnit.Celsius -> convertKelvinToCelsius(kelvin)
        }

        return raw.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
    }

    private fun convertKelvinToFahrenheit(kelvin: Double): Double {
        return kelvin * 9 / 5 - 459.67
    }

    private fun convertKelvinToCelsius(kelvin: Double): Double {
        return kelvin - 273.15
    }
}