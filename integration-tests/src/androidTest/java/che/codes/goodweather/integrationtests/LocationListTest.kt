package che.codes.goodweather.integrationtests

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import che.codes.androidtesting.AndroidFileUtils.getJsonFromFile
import che.codes.androidtesting.IdlingSchedulerRule
import che.codes.androidtesting.MatcherUtils.hasRecyclerItemCount
import che.codes.goodweather.features.locationlist.LocationListFragment
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationListTest {

    @get:Rule
    val schedulerRule = IdlingSchedulerRule()

    private val navControllerMock: NavController = mock()

    @Test
    fun onLaunch_citiesStored_showCities() {
        storeCities()

        launchFragment()

        onView(withId(R.id.list_locations)).check(matches(hasRecyclerItemCount(2)))
    }

    @Test
    fun onLaunch_noCitiesStored_showNoCities() {
        launchFragment()

        onView(withId(R.id.list_locations)).check(matches(hasRecyclerItemCount(0)))
    }

    @Test
    fun clickAddButton_navigateToAddLocation() {
        launchFragment()

        onView(withId(R.id.button_add)).perform(click())

        verify(navControllerMock).navigate(R.id.action_add_location)
    }

    @After
    fun tearDown() {
        clearSharedPrefs()
    }

    //region Helper Methods

    private fun launchFragment(): FragmentScenario<LocationListFragment> {
        return launchFragmentInContainer<LocationListFragment>(Bundle(), R.style.Theme_MaterialComponents).apply {
            onFragment { fragment ->
                Navigation.setViewNavController(fragment.requireView(), navControllerMock)
            }
        }
    }

    private fun storeCities() {
        sharedPrefs().edit().putString("CITIES", getJsonFromFile("cities_list.json")).commit()
    }

    private fun sharedPrefs(): SharedPreferences {
        return InstrumentationRegistry.getInstrumentation().targetContext.getSharedPreferences(
            "CITIES",
            Context.MODE_PRIVATE
        )
    }

    private fun clearSharedPrefs() {
        sharedPrefs().edit().clear().commit()
    }

    //endregion
}