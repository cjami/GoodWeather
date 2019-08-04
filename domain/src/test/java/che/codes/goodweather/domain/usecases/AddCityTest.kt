package che.codes.goodweather.domain.usecases

import che.codes.goodweather.domain.CityRepository
import che.codes.goodweather.domain.models.City
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class AddCityTest {
    private lateinit var sut: AddCity
    private val repositoryMock: CityRepository = mock()
    private val testCity = City("NAME", City.Country("LONG", "SHORT"), 5.0, 5.0)

    @BeforeEach
    fun setUp() {
        sut = AddCity(repositoryMock)
    }

    @Nested
    inner class Invoke {

        @Test
        fun `interacts with repository`() {
            sut.invoke(testCity)

            verify(repositoryMock).store(testCity)
        }
    }
}