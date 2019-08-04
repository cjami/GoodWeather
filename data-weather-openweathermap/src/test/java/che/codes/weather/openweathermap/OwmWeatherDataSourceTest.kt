package che.codes.weather.openweathermap

import che.codes.goodweather.domain.models.Weather
import che.codes.testing.FileUtils.getObjectFromFile
import che.codes.testing.RxTestUtils.assertError
import che.codes.testing.RxTestUtils.getResult
import che.codes.weather.openweathermap.models.OwmWeatherPayload
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

internal class OwmWeatherDataSourceTest {

    private lateinit var sut: OwmWeatherDataSource
    private val serviceMock: OwmApiService = mock()
    private val testApiId = "API_ID"
    private val testPayload = getObjectFromFile("owm_success_payload.json", OwmWeatherPayload::class.java)
    private val expectedWeather = getObjectFromFile("expected_weather.json", Weather::class.java)

    @BeforeEach
    fun setUp() {
        sut = OwmWeatherDataSource(serviceMock, testApiId)
    }

    @Nested
    inner class GetForecast {
        @Test
        fun `interacts with service`() {
            success()

            sut.getForecast(0.0, 0.0, 4)

            verify(serviceMock).getForecast(0.0, 0.0, 4, testApiId)
        }

        @Test
        fun `returns correct weather on service success`() {
            success()

            val result = getResult(sut.getForecast(0.0, 0.0, 4))

            assertThat(result[0], equalTo(expectedWeather))
        }

        @Test
        fun `returns error on service error`() {
            val e = Throwable("General Error")
            error(e)

            assertError(sut.getForecast(0.0, 0.0, 4), e)
        }
    }

    //region Helper Methods

    private fun success() {
        whenever(serviceMock.getForecast(any(), any(), any(), any())).thenReturn(Single.just(testPayload))
    }

    private fun error(e: Throwable) {
        whenever(serviceMock.getForecast(any(), any(), any(), any())).thenReturn(Single.error(e))
    }

    //endregion
}