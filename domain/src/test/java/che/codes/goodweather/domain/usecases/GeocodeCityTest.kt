package che.codes.goodweather.domain.usecases

import che.codes.goodweather.domain.CityRepository
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

internal class GeocodeCityTest {

    private lateinit var sut: GeocodeCity
    private val repositoryMock: CityRepository = mock()
    private val testCity = City("NAME", City.Country("LONG", "SHORT"), 5.0, 5.0)

    @BeforeEach
    fun setUp() {
        sut = GeocodeCity(repositoryMock)
    }

    @Nested
    inner class Invoke {

        @Test
        fun `interacts with repository`() {
            success()

            waitForTerminalEvent(sut.invoke(testCity.name))

            verify(repositoryMock).geocode(testCity.name)
        }

        @Test
        fun `emits correct city on success`() {
            success()

            val result = getResult(sut.invoke(testCity.name))

            assertThat(result, equalTo(testCity))
        }

        @Test
        fun `emits same error on error`() {
            val e = Throwable("General Error")
            error(e)

            val single = sut.invoke(testCity.name)

            assertError(single, e)
        }
    }

    //region Helper Methods

    private fun success() {
        whenever(repositoryMock.geocode(any())).thenReturn(Single.just(testCity))
    }

    private fun error(e: Throwable) {
        whenever(repositoryMock.geocode(any())).thenReturn(Single.error(e))
    }

    //endregion
}