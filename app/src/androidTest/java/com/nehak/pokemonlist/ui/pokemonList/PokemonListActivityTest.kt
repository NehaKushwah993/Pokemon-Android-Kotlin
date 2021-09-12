package com.nehak.pokemonlist.ui.pokemonList

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nehak.pokemonlist.R
import com.nehak.pokemonlist.util.MockUtilAndroidTest.mockPokemonList
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class PokemonListActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<PokemonListActivity> =
        ActivityScenarioRule(PokemonListActivity::class.java)
    private lateinit var activity: PokemonListActivity

    @Before
    fun init() {
        activityRule.scenario.onActivity {
            activity = it
        }
        Thread.sleep(1000)
    }

    @Test
    fun isPokemonRecyclerViewVisible() {
        onView(withId(R.id.rv_pokemon_list)).check(matches(isDisplayed()))
    }

    @Test
    fun checkErrorSnackBarVisibility() {
        activity.viewModel.setErrorMessage("Error")
        onView(withText("Error")).check(matches(isDisplayed()))
    }

    @Test
    fun checkCenterErrorViewVisibility() {
        activity.viewBinding.showCenterError = true
        onView(withId(R.id.layout_error)).check(matches(isDisplayed()))

        activity.viewBinding.showCenterError = false
        onView(withId(R.id.layout_error)).check(matches(not(isDisplayed())))
    }
}