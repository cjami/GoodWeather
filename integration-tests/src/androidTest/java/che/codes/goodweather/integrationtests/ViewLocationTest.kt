package che.codes.goodweather.integrationtests

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import che.codes.androidtesting.AndroidFileUtils.getJsonFromFile
import che.codes.androidtesting.AndroidFileUtils.getListFromFile
import che.codes.androidtesting.AndroidFileUtils.getObjectFromFile
import che.codes.androidtesting.IdlingSchedulerRule
import che.codes.androidtesting.MockServerUtils.createJsonResponse
import che.codes.goodweather.core.di.AppContextModule
import che.codes.goodweather.core.di.CoreComponent
import che.codes.goodweather.core.models.CityParcel
import che.codes.goodweather.domain.SETTINGS_TEMP_UNIT
import che.codes.goodweather.domain.models.Weather
import che.codes.goodweather.features.viewlocation.ViewLocationFragment
import che.codes.goodweather.integrationtests.di.DaggerTestCoreComponent
import che.codes.goodweather.integrationtests.di.TestPropertyModule
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.roundToInt

@RunWith(AndroidJUnit4::class)
class ViewLocationTest {

    @get:Rule
    val schedulerRule = IdlingSchedulerRule()

    private lateinit var mockServer: MockWebServer

    private val expectedWeatherF = getListFromFile("expected_weather_fahrenheit.json", Array<Weather>::class.java)
    private val expectedWeatherC = getListFromFile("expected_weather_celsius.json", Array<Weather>::class.java)
    private val testCityArg = getObjectFromFile("single_city.json", CityParcel::class.java)

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext

        val component = DaggerTestCoreComponent.builder()
            .appContextModule(AppContextModule(appContext))
            .testPropertyModule(TestPropertyModule(openweatherApiUrl = mockServer.url("").toString()))
            .build()

        (appContext as CoreComponent.Provider).setCoreComponent(component)
    }

    @Test
    fun onLaunch_onSuccess_fahrenheitSetting_correctForecastDisplayed() {
        fahrenheitSetting()
        success()

        launchFragment()

        assertCorrectMainWeather(expectedWeatherF[0])
        assertCorrectWeather(R.id.day_two, expectedWeatherF[1])
        assertCorrectWeather(R.id.day_three, expectedWeatherF[2])
        assertCorrectWeather(R.id.day_four, expectedWeatherF[3])
    }

    @Test
    fun onLaunch_onSuccess_celsiusSetting_correctForecastDisplayed() {
        celsiusSetting()
        success()

        launchFragment()

        assertCorrectMainWeather(expectedWeatherC[0])
        assertCorrectWeather(R.id.day_two, expectedWeatherC[1])
        assertCorrectWeather(R.id.day_three, expectedWeatherC[2])
        assertCorrectWeather(R.id.day_four, expectedWeatherC[3])
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
        clearSharedPrefs()
    }

    //region Helper Methods

    private fun launchFragment(): FragmentScenario<ViewLocationFragment> {
        val bundle = Bundle().apply { putParcelable("ARG_CITY", testCityArg) }
        return launchFragmentInContainer(bundle, R.style.Theme_MaterialComponents)
    }

    private fun assertCorrectMainWeather(weather: Weather) {
        onView(
            allOf(withId(R.id.text_day), withParent(withId(R.id.card_layout)))
        ).check(matches(withText(weather.day.toLowerCase().capitalize())))

        onView(
            allOf(withId(R.id.text_temp), withParent(withId(R.id.card_layout)))
        ).check(matches(withText(weather.temp.roundToInt().toString() + tempSymbol())))
    }

    private fun assertCorrectWeather(parentId: Int, weather: Weather) {
        onView(
            allOf(withId(R.id.text_day), withParent(withId(parentId)))
        ).check(matches(withText(weather.day.substring(0..2).toUpperCase())))

        onView(
            allOf(withId(R.id.text_temp), withParent(withId(parentId)))
        ).check(matches(withText(weather.temp.roundToInt().toString() + tempSymbol())))
    }

    private fun tempSymbol(): Char {
        return when (sharedPrefs().getBoolean(SETTINGS_TEMP_UNIT, false)) {
            true -> '\u2109'
            false -> '\u2103'
        }
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

    private fun success() {
        mockServer.enqueue(createJsonResponse(200, getJsonFromFile("owm_success_payload.json")))
    }

    private fun fahrenheitSetting() {
        sharedPrefs().edit().putBoolean(SETTINGS_TEMP_UNIT, true).commit()
    }

    private fun celsiusSetting() {
        sharedPrefs().edit().putBoolean(SETTINGS_TEMP_UNIT, false).commit()
    }

    //endregion
}