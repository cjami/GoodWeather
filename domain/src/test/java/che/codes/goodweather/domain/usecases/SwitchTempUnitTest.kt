package che.codes.goodweather.domain.usecases

import che.codes.goodweather.domain.SETTINGS_TEMP_UNIT
import che.codes.goodweather.domain.SettingsRepository
import che.codes.goodweather.domain.models.TempUnit
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class SwitchTempUnitTest {

    private lateinit var sut: SwitchTempUnit
    private val repositoryMock: SettingsRepository = mock()

    @BeforeEach
    fun setUp() {
        sut = SwitchTempUnit(repositoryMock)
    }

    @Nested
    inner class Invoke {

        @Test
        fun `puts true in repository for fahrenheit`() {
            sut.invoke(TempUnit.Fahrenheit)

            verify(repositoryMock).put(SETTINGS_TEMP_UNIT, value = true)
        }

        @Test
        fun `puts false in repository for celsius`() {
            sut.invoke(TempUnit.Celsius)

            verify(repositoryMock).put(SETTINGS_TEMP_UNIT, value = false)
        }
    }
}