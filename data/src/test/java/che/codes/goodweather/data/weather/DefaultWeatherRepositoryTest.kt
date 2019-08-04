package che.codes.goodweather.data.weather

import che.codes.goodweather.domain.models.Weather
import che.codes.testing.RxTestUtils.assertError
import che.codes.testing.RxTestUtils.getResult
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

internal class DefaultWeatherRepositoryTest {

    private lateinit var sut: DefaultWeatherRepository
    private val sourceMock: WeatherDataSource = mock()
    private val testWeather = Weather("SUNNY", "SUNNY", 290.0, "MONDAY", "01a")

    @BeforeEach
    fun setUp() {
        sut = DefaultWeatherRepository(sourceMock)
    }

    @Nested
    inner class GetForecast {

        @Test
        fun `interacts with source`() {
            success()

            waitForTerminalEvent(sut.getForecast(2.0, 2.0, 4))

            verify(sourceMock).getForecast(2.0, 2.0, 4)
        }

        @Test
        fun `emits correct weather on success`() {
            success()

            val result = getResult(sut.getForecast(2.0, 2.0, 4))

            assertThat(result, equalTo(listOf(testWeather)))
        }

        @Test
        fun `emits same error on error`() {
            val e = Throwable("General Error")
            error(e)

            val single = sut.getForecast(2.0, 2.0, 4)

            assertError(single, e)
        }
    }

    //region Helper Methods

    private fun success() {
        whenever(sourceMock.getForecast(any(), any(), any())).thenReturn(Single.just(listOf(testWeather)))
    }

    private fun error(e: Throwable) {
        whenever(sourceMock.getForecast(any(), any(), any())).thenReturn(Single.error(e))
    }

    //endregion
}