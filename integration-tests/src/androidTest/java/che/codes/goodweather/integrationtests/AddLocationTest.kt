package che.codes.goodweather.integrationtests

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import che.codes.androidtesting.AndroidFileUtils.getJsonFromFile
import che.codes.androidtesting.IdlingSchedulerRule
import che.codes.androidtesting.MockServerUtils.createJsonResponse
import che.codes.androidtesting.TestComputationSchedulerRule
import che.codes.goodweather.core.di.AppContextModule
import che.codes.goodweather.core.di.CoreComponent
import che.codes.goodweather.features.addlocation.AddLocationFragment
import che.codes.goodweather.features.addlocation.TEXT_CHANGE_DEBOUNCE
import che.codes.goodweather.integrationtests.di.DaggerTestCoreComponent
import che.codes.goodweather.integrationtests.di.TestPropertyModule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class AddLocationTest {

    @get:Rule
    val schedulerRule = IdlingSchedulerRule()

    @get:Rule
    val testSchedulerRule = TestComputationSchedulerRule()

    private lateinit var mockServer: MockWebServer

    private val menuAddItemMock: MenuItem = mock()
    private val navControllerMock: NavController = mock()

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext

        val component = DaggerTestCoreComponent.builder()
            .appContextModule(AppContextModule(appContext))
            .testPropertyModule(TestPropertyModule(geocoderApiUrl = mockServer.url("").toString()))
            .build()

        (appContext as CoreComponent.Provider).setCoreComponent(component)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
        clearSharedPrefs()
    }

    @Test
    fun validCityNameInput_countryDisplayed() {
        geocodeSuccess()
        launchFragment()

        onView(withId(R.id.edit_city_name)).perform(typeText("Mountain View"))
        testSchedulerRule.testScheduler.advanceTimeBy(TEXT_CHANGE_DEBOUNCE, TimeUnit.MILLISECONDS)

        onView(withId(R.id.text_city_country)).check(matches(withText("United States")))
    }

    @Test
    fun invalidCityNameInput_countryNotDisplayed() {
        geocodeNoResults()
        launchFragment()

        onView(withId(R.id.edit_city_name)).perform(typeText("thisisnotacity"))
        testSchedulerRule.testScheduler.advanceTimeBy(TEXT_CHANGE_DEBOUNCE, TimeUnit.MILLISECONDS)

        onView(withId(R.id.text_city_country)).check(matches(not(isDisplayed())))
    }

    @Test
    fun addAction_locationAdded() {
        geocodeSuccess()
        val fragmentScenario = launchFragment()
        setupAddAction(fragmentScenario)
        onView(withId(R.id.edit_city_name)).perform(typeText("Mountain View"))
        testSchedulerRule.testScheduler.advanceTimeBy(TEXT_CHANGE_DEBOUNCE, TimeUnit.MILLISECONDS)
        onIdle()

        fragmentScenario.onFragment {
            it.onOptionsItemSelected(menuAddItemMock)
        }

        assert(sharedPrefs().getString("CITIES", null) != null)
    }

    @Test
    fun addAction_popBackStack() {
        geocodeSuccess()
        val fragmentScenario = launchFragment()
        setupAddAction(fragmentScenario)
        onView(withId(R.id.edit_city_name)).perform(typeText("Mountain View"))
        testSchedulerRule.testScheduler.advanceTimeBy(TEXT_CHANGE_DEBOUNCE, TimeUnit.MILLISECONDS)
        onIdle()

        fragmentScenario.onFragment {
            it.onOptionsItemSelected(menuAddItemMock)
        }

        verify(navControllerMock).popBackStack()
    }

    //region Helper Methods

    private fun geocodeSuccess() {
        mockServer.enqueue(createJsonResponse(200, getJsonFromFile("geocoder_success.json")))
    }

    private fun geocodeNoResults() {
        mockServer.enqueue(createJsonResponse(200, getJsonFromFile("geocoder_no_results.json")))
    }

    private fun launchFragment():FragmentScenario<AddLocationFragment>{
        return launchFragmentInContainer(Bundle(), R.style.Theme_AppCompat)
    }

    private fun setupAddAction(fragmentScenario: FragmentScenario<AddLocationFragment>) {
        whenever(menuAddItemMock.itemId).thenReturn(R.id.action_add_location)

        fragmentScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navControllerMock)
        }
    }

    private fun sharedPrefs(): SharedPreferences {
        return InstrumentationRegistry.getInstrumentation().targetContext.getSharedPreferences("CITIES", MODE_PRIVATE)
    }

    private fun clearSharedPrefs() {
        sharedPrefs().edit().clear().commit()
    }

    //endregion
}