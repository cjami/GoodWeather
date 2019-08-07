package che.codes.goodweather.domain.usecases

import che.codes.goodweather.domain.SettingsRepository
import che.codes.goodweather.domain.WeatherRepository
import che.codes.goodweather.domain.models.City
import che.codes.goodweather.domain.models.TempUnit
import che.codes.goodweather.domain.models.Weather
import che.codes.testing.RxTestUtils
import che.codes.testing.RxTestUtils.assertError
import che.codes.testing.RxTestUtils.waitForTerminalEvent
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GetForecastTest {

    private lateinit var sut: GetForecast
    private val weatherRepositoryMock: WeatherRepository = mock()
    private val settingsRepositoryMock: SettingsRepository = mock()
    private val testCity = City("NAME", City.Country("LONG", "SHORT"), 5.0, 5.0)
    private val testForecast = listOf(Weather("SUNNY", "SUNNY", 273.15, "MONDAY", "01a"))
    private val expectedForecastF = listOf(Weather("SUNNY", "SUNNY", 32.0, "MONDAY", "01a"))
    private val expectedForecastC = listOf(Weather("SUNNY", "SUNNY", 0.0, "MONDAY", "01a"))

    @BeforeEach
    fun setUp() {
        sut = GetForecast(weatherRepositoryMock, settingsRepositoryMock)
    }

    @Nested
    inner class Invoke {

        @Test
        fun `interacts with repository`() {
            successFahrenheit()

            waitForTerminalEvent(sut.invoke(testCity))

            verify(weatherRepositoryMock).getForecast(testCity.latitude, testCity.longitude)
        }

        @Test
        fun `emits correct weather on success with Fahrenheit`() {
            successFahrenheit()

            val result = RxTestUtils.getResult(sut.invoke(testCity))

            assertThat(result, equalTo(GetForecast.ForecastWithTempUnit(expectedForecastF, TempUnit.Fahrenheit)))
        }

        @Test
        fun `emits correct weather on success with Celsius`() {
            successCelsius()

            val result = RxTestUtils.getResult(sut.invoke(testCity))

            assertThat(result, equalTo(GetForecast.ForecastWithTempUnit(expectedForecastC, TempUnit.Celsius)))
        }

        @Test
        fun `emits same error on error`() {
            val e = Throwable("General Error")
            error(e)

            val single = sut.invoke(testCity)

            assertError(single, e)
        }
    }

    //region Helper Methods

    private fun successFahrenheit() {
        whenever(weatherRepositoryMock.getForecast(any(), any())).thenReturn(Single.just(testForecast))
        whenever(settingsRepositoryMock.getBoolean(any())).thenReturn(Single.just(true))
    }

    private fun successCelsius(){
        whenever(weatherRepositoryMock.getForecast(any(), any())).thenReturn(Single.just(testForecast))
        whenever(settingsRepositoryMock.getBoolean(any())).thenReturn(Single.just(false))
    }

    private fun error(e: Throwable) {
        whenever(weatherRepositoryMock.getForecast(any(), any())).thenReturn(Single.error(e))
        whenever(settingsRepositoryMock.getBoolean(any())).thenReturn(Single.just(true))
    }

    //endregion
}