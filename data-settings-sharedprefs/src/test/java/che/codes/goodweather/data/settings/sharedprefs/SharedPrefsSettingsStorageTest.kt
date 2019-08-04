package che.codes.goodweather.data.settings.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import che.codes.testing.RxTestUtils.getResult
import che.codes.testing.RxTestUtils.waitForTerminalEvent
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class SharedPrefsSettingsStorageTest {

    private lateinit var sut: SharedPrefsSettingsStorage
    private val contextMock: Context = mock()
    private val sharedPrefsMock: SharedPreferences = mock()
    private val editorMock: SharedPreferences.Editor = mock()

    @BeforeEach
    fun setUp() {
        whenever(contextMock.getSharedPreferences(any(), any())).thenReturn(sharedPrefsMock)
        whenever(sharedPrefsMock.edit()).thenReturn(editorMock)
        whenever(sharedPrefsMock.getBoolean(any(), any())).thenReturn(true)

        sut = SharedPrefsSettingsStorage(contextMock)
    }

    @Nested
    inner class Store {

        @Test
        fun `interacts with shared preferences`() {
            sut.store("SETTING1", true)

            verify(sharedPrefsMock).edit()
        }

        @Test
        fun `stores correct settings in editor`() {
            sut.store("SETTING1", true)
            sut.store("SETTING2", false)

            verify(editorMock).putBoolean("SETTING1", true)
            verify(editorMock).putBoolean("SETTING2", false)
        }

        @Test
        fun `calls commit after editing`() {
            sut.store("SETTING1", true)

            verify(editorMock).commit()
        }
    }

    @Nested
    inner class Retrieve {

        @Test
        fun `interacts with shared preferences`() {
            waitForTerminalEvent(sut.retrieveBoolean("SETTING1"))

            verify(sharedPrefsMock).getBoolean("SETTING1", false)
        }

        @Test
        fun `retrieves correct settings from shared preferences`() {
            whenever(sharedPrefsMock.getBoolean(any(), any())).thenReturn(true)

            val result = getResult(sut.retrieveBoolean("SETTING1"))

            assertThat(result, equalTo(true))
        }
    }
}