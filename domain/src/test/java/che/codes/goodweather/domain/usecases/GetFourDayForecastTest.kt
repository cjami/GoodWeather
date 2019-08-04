package che.codes.goodweather.domain.usecases

import che.codes.goodweather.domain.WeatherRepository
import che.codes.goodweather.domain.models.City
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

internal class GetFourDayForecastTest {

    private lateinit var sut: GetFourDayForecast
    private val repositoryMock: WeatherRepository = mock()
    private val testCity = City("NAME", City.Country("LONG", "SHORT"), 5.0, 5.0)
    private val testWeather = listOf(Weather("SUNNY", "SUNNY", 290.0, "MONDAY", "01a"))

    @BeforeEach
    fun setUp() {
        sut = GetFourDayForecast(repositoryMock)
    }

    @Nested
    inner class Invoke {

        @Test
        fun `interacts with repository`() {
            success()

            waitForTerminalEvent(sut.invoke(testCity))

            verify(repositoryMock).getForecast(testCity.latitude, testCity.longitude, days = 4)
        }

        @Test
        fun `emits correct weather on success`() {
            success()

            val result = RxTestUtils.getResult(sut.invoke(testCity))

            assertThat(result, equalTo(testWeather))
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

    private fun success() {
        whenever(repositoryMock.getForecast(any(), any(), any())).thenReturn(Single.just(testWeather))
    }

    private fun error(e: Throwable) {
        whenever(repositoryMock.getForecast(any(), any(), any())).thenReturn(Single.error(e))
    }

    //endregion
}