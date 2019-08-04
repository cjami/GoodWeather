package che.codes.goodweather.domain.usecases

import che.codes.goodweather.domain.CityRepository
import che.codes.goodweather.domain.models.City
import che.codes.testing.RxTestUtils.assertError
import che.codes.testing.RxTestUtils.getResult
import che.codes.testing.RxTestUtils.waitForTerminalEvent
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class LoadCitiesTest {

    private lateinit var sut: LoadCities
    private val repositoryMock: CityRepository = mock()
    private val testCities = listOf(City("NAME", City.Country("LONG", "SHORT"), 5.0, 5.0))

    @BeforeEach
    fun setUp() {
        sut = LoadCities(repositoryMock)
    }

    @Nested
    inner class Invoke {

        @Test
        fun `interacts with repository`() {
            success()

            waitForTerminalEvent(sut.invoke())

            verify(repositoryMock).retrieve()
        }

        @Test
        fun `emits correct cities on success`() {
            success()

            val result = getResult(sut.invoke())

            assertThat(result, equalTo(testCities))
        }

        @Test
        fun `emits same error on error`() {
            val e = Throwable("General Error")
            error(e)

            val single = sut.invoke()

            assertError(single, e)
        }
    }

    //region Helper Methods

    private fun success() {
        whenever(repositoryMock.retrieve()).thenReturn(Single.just(testCities))
    }

    private fun error(e: Throwable) {
        whenever(repositoryMock.retrieve()).thenReturn(Single.error(e))
    }

    //endregion
}