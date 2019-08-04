package che.codes.goodweather.data.settings

import che.codes.goodweather.domain.SettingsRepository
import che.codes.goodweather.domain.models.Weather
import che.codes.testing.RxTestUtils
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

internal class DefaultSettingsRepositoryTest {

    private lateinit var sut: SettingsRepository
    private val storageMock: SettingsStorage = mock()
    private val testKey = "TEST_KEY"

    @BeforeEach
    fun setUp() {
        sut = DefaultSettingsRepository(storageMock)
    }

    @Nested
    inner class Put {

        @Test
        fun `interacts with storage`() {
            sut.put(testKey, value = true)

            verify(storageMock).store(testKey, value = true)
        }
    }

    @Nested
    inner class GetBoolean {

        @Test
        fun `interacts with storage`() {
            success()

            waitForTerminalEvent(sut.getBoolean(testKey))

            verify(storageMock).retrieveBoolean(testKey)
        }

        @Test
        fun `emits correct boolean on success`() {
            success()

            val result = getResult(sut.getBoolean(testKey))

            assertThat(result, equalTo(true))
        }

        @Test
        fun `emits same error on error`() {
            val e = Throwable("General Error")
            error(e)

            val single = sut.getBoolean(testKey)

            RxTestUtils.assertError(single, e)
        }
    }

    //region Helper Methods

    private fun success() {
        whenever(storageMock.retrieveBoolean(any())).thenReturn(Single.just(true))
    }

    private fun error(e: Throwable) {
        whenever(storageMock.retrieveBoolean(any())).thenReturn(Single.error(e))
    }

    //endregion
}