package che.codes.goodweather.data.city.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import che.codes.goodweather.domain.models.City
import che.codes.testing.RxTestUtils.getResult
import che.codes.testing.RxTestUtils.waitForTerminalEvent
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class SharedPrefsCityStorageTest {

    private lateinit var sut: SharedPrefsCityStorage
    private val contextMock: Context = mock()
    private val sharedPrefsMock: SharedPreferences = mock()
    private val editorMock: SharedPreferences.Editor = mock()
    private val testCity = City("NAME", City.Country("LONG", "SHORT"), 5.0, 5.0)
    private val testCitiesJson = Gson().toJson(listOf(testCity))

    @BeforeEach
    fun setUp() {
        whenever(contextMock.getSharedPreferences(any(), any())).thenReturn(sharedPrefsMock)
        whenever(sharedPrefsMock.edit()).thenReturn(editorMock)
        whenever(sharedPrefsMock.getString(any(), any())).thenReturn(testCitiesJson)

        sut = SharedPrefsCityStorage(contextMock)
    }

    @Nested
    inner class Store {

        @Test
        fun `interacts with shared preferences`() {
            sut.store(testCity)

            verify(sharedPrefsMock).edit()
        }

        @Test
        fun `stores correct cities in editor`() {
            sut.store(testCity)

            verify(editorMock).putString(CITIES_KEY, testCitiesJson)
        }

        @Test
        fun `calls apply after editing`() {
            sut.store(testCity)

            verify(editorMock).apply()
        }
    }

    @Nested
    inner class Retrieve {

        @Test
        fun `interacts with shared preferences`() {
            waitForTerminalEvent(sut.retrieve())

            verify(sharedPrefsMock).getString(CITIES_KEY, "[]")
        }

        @Test
        fun `retrieves correct cities from shared preferences`() {
            val result = getResult(sut.retrieve())

            assertThat(result, equalTo(listOf(testCity)))
        }
    }
}