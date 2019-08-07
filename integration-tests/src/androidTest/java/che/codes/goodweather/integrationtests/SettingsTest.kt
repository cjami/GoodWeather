package che.codes.goodweather.integrationtests

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import che.codes.androidtesting.IdlingSchedulerRule
import che.codes.goodweather.domain.SETTINGS_TEMP_UNIT
import che.codes.goodweather.features.settings.SettingsFragment
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Rule
import org.junit.Test

class SettingsTest {

    @get:Rule
    val schedulerRule = IdlingSchedulerRule()

    @After
    fun tearDown() {
        clearSharedPrefs()
    }

    @Test
    fun onLaunch_withFahrenheitSetting_settingChecked() {
        fahrenheitSetting()

        launchFragment()

        onView(withId(R.id.switch_setting_temp)).check(matches(isChecked()))
    }

    @Test
    fun onLaunch_withCelsiusSetting_settingNotChecked() {
        celsiusSetting()

        launchFragment()

        onView(withId(R.id.switch_setting_temp)).check(matches(not(isChecked())))
    }

    @Test
    fun switchOff_withFahrenheitSetting_setsFalse() {
        fahrenheitSetting()
        launchFragment()

        onView(withId(R.id.switch_setting_temp)).perform(click())

        assertThat(sharedPrefs().getBoolean(SETTINGS_TEMP_UNIT, true), equalTo(false))
    }

    @Test
    fun switchOn_withCelsiusSetting_setsTrue() {
        celsiusSetting()
        launchFragment()

        onView(withId(R.id.switch_setting_temp)).perform(click())

        assertThat(sharedPrefs().getBoolean(SETTINGS_TEMP_UNIT, false), equalTo(true))
    }

    //region Helper Methods

    private fun launchFragment(): FragmentScenario<SettingsFragment> {
        return launchFragmentInContainer(Bundle(), R.style.Theme_MaterialComponents)
    }

    private fun sharedPrefs(): SharedPreferences {
        return InstrumentationRegistry.getInstrumentation().targetContext.getSharedPreferences(
            "SETTINGS",
            Context.MODE_PRIVATE
        )
    }

    private fun clearSharedPrefs() {
        sharedPrefs().edit().clear().commit()
    }

    private fun fahrenheitSetting() {
        sharedPrefs().edit().putBoolean(SETTINGS_TEMP_UNIT, true).commit()
    }

    private fun celsiusSetting() {
        sharedPrefs().edit().putBoolean(SETTINGS_TEMP_UNIT, false).commit()
    }

    //endregion
}