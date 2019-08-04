package che.codes.goodweather.data.city

import che.codes.goodweather.domain.models.City
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

internal class DefaultCityRepositoryTest {

    private lateinit var sut: DefaultCityRepository
    private val geocoderMock: CityGeocoder = mock()
    private val storageMock: CityStorage = mock()
    private val testCity = City("NAME", City.Country("LONG", "SHORT"), 5.0, 5.0)

    @BeforeEach
    fun setUp() {
        sut = DefaultCityRepository(geocoderMock, storageMock)
    }

    @Nested
    inner class Geocode {

        @Test
        fun `interacts with geocoder`() {
            geocoderSuccess()

            waitForTerminalEvent(sut.geocode("NAME"))

            verify(geocoderMock).geocode("NAME")
        }

        @Test
        fun `emits correct city on geocoder success`() {
            geocoderSuccess()

            val result = getResult(sut.geocode("NAME"))

            assertThat(result, equalTo(testCity))
        }

        @Test
        fun `emits same error on geocoder error`() {
            val e = Throwable("General Error")
            geocoderError(e)

            val single = sut.geocode("NAME")

            assertError(single, e)
        }
    }

    @Nested
    inner class Store {

        @Test
        fun `interacts with storage`() {
            sut.store(testCity)

            verify(storageMock).store(testCity)
        }
    }

    @Nested
    inner class Retrieve {

        @Test
        fun `interacts with storage`() {
            storageRetrieveSuccess()

            waitForTerminalEvent(sut.retrieve())

            verify(storageMock).retrieve()
        }

        @Test
        fun `emits correct city on storage success`() {
            storageRetrieveSuccess()

            val result = getResult(sut.retrieve())

            assertThat(result, equalTo(listOf(testCity)))
        }

        @Test
        fun `emits same error on storage error`() {
            val e = Throwable("General Error")
            storageRetrieveError(e)

            val single = sut.retrieve()

            assertError(single, e)
        }
    }

    //region Helper Methods

    fun geocoderSuccess() {
        whenever(geocoderMock.geocode(any())).thenReturn(Single.just(testCity))
    }

    fun geocoderError(e: Throwable) {
        whenever(geocoderMock.geocode(any())).thenReturn(Single.error(e))
    }

    fun storageRetrieveSuccess() {
        whenever(storageMock.retrieve()).thenReturn(Single.just(listOf(testCity)))
    }

    fun storageRetrieveError(e: Throwable) {
        whenever(storageMock.retrieve()).thenReturn(Single.error(e))
    }

    //endregion
}