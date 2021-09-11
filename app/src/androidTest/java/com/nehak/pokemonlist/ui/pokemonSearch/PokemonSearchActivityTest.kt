package com.nehak.pokemonlist.ui.pokemonSearch

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nehak.pokemonlist.R
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
class PokemonSearchActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<PokemonSearchActivity> =
        ActivityScenarioRule(PokemonSearchActivity::class.java)
    lateinit var activity: PokemonSearchActivity;

    @Before
    fun init() {
        activityRule.scenario.onActivity {
            activity = it
        }
    }

    private fun getViewAssertion(visibility: Visibility): ViewAssertion? {
        return matches(withEffectiveVisibility(visibility))
    }

    fun isGone() = getViewAssertion(Visibility.GONE)

    fun isVisible() = getViewAssertion(Visibility.VISIBLE)

    fun isInvisible() = getViewAssertion(Visibility.INVISIBLE)

    @Test
    fun isPokemonRecyclerViewVisible() {
        onView(withId(R.id.rv_pokemon_list)).check(matches(isDisplayed()))
        onView(withId(R.id.layout_no_pokemon_found)).check(matches(not(isDisplayed())))
    }

    @Test
    fun checkCenterErrorViewVisibility() {
        activity.viewBinding.foundNoData = true
        onView(withId(R.id.layout_no_pokemon_found)).check(matches(isDisplayed()))
        activity.viewBinding.foundNoData = true
        onView(withId(R.id.layout_no_pokemon_found)).check(matches(not(isDisplayed())))
    }

}