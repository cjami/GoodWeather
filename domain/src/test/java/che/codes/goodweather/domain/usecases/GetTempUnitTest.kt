package che.codes.goodweather.domain.usecases

import che.codes.goodweather.domain.SETTINGS_TEMP_UNIT
import che.codes.goodweather.domain.SettingsRepository
import che.codes.goodweather.domain.models.TempUnit
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

internal class GetTempUnitTest {


    private lateinit var sut: GetTempUnit
    private val repositoryMock: SettingsRepository = mock()

    @BeforeEach
    fun setUp() {
        sut = GetTempUnit(repositoryMock)
    }

    @Nested
    inner class Invoke {

        @Test
        fun `interacts with repository`() {
            successCelsius()

            waitForTerminalEvent(sut.invoke())

            verify(repositoryMock).getBoolean(SETTINGS_TEMP_UNIT)
        }

        @Test
        fun `emits fahrenheit when boolean from repository is true`() {
            successFahrenheit()

            val result = getResult(sut.invoke())

            assertThat(result, equalTo(TempUnit.Fahrenheit))
        }

        @Test
        fun `emits celsius when boolean from repository is false`() {
            successCelsius()

            val result = getResult(sut.invoke())

            assertThat(result, equalTo(TempUnit.Celsius))
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

    private fun successCelsius() {
        whenever(repositoryMock.getBoolean(any())).thenReturn(Single.just(false))
    }

    private fun successFahrenheit() {
        whenever(repositoryMock.getBoolean(any())).thenReturn(Single.just(true))
    }

    private fun error(e: Throwable) {
        whenever(repositoryMock.getBoolean(any())).thenReturn(Single.error(e))
    }

    //endregion
}