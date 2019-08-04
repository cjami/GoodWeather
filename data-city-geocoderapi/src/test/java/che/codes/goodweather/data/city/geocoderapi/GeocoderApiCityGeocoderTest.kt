package che.codes.goodweather.data.city.geocoderapi

import che.codes.goodweather.data.city.geocoderapi.models.GeocodeResultPayload
import che.codes.goodweather.domain.models.City
import che.codes.testing.FileUtils.getObjectFromFile
import che.codes.testing.RxTestUtils.assertError
import che.codes.testing.RxTestUtils.getResult
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

internal class GeocoderApiCityGeocoderTest {

    private lateinit var sut: GeocoderApiCityGeocoder
    private val serviceMock: GeocoderApiService = mock()
    private val testApiKey = "API_KEY"
    private val testCityName = "Mountain View"
    private val testPayload = getObjectFromFile("geocode_success_payload.json", GeocodeResultPayload::class.java)
    private val expectedCity = getObjectFromFile("expected_city.json", City::class.java)

    @BeforeEach
    fun setUp() {
        sut = GeocoderApiCityGeocoder(serviceMock, testApiKey)
    }

    @Nested
    inner class Geocode{

        @Test
        fun `interacts with service`() {
            success()

            sut.geocode(testCityName)

            verify(serviceMock).geocode(testCityName, testApiKey)
        }

        @Test
        fun `returns correct city on service success`() {
            success()

            val result = getResult(sut.geocode(testCityName))

            assertThat(result, equalTo(expectedCity))
        }

        @Test
        fun `returns error on service error`() {
            val e = Throwable("General Error")
            error(e)

            assertError(sut.geocode(testCityName), e)
        }
    }

    //region Helper Methods

    private fun success() {
        whenever(serviceMock.geocode(any(), any())).thenReturn(Single.just(testPayload))
    }

    private fun error(e: Throwable) {
        whenever(serviceMock.geocode(any(), any())).thenReturn(Single.error(e))
    }

    //endregion
}