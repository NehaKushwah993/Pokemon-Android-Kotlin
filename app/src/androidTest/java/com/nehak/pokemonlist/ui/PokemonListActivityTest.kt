package com.nehak.pokemonlist.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nehak.pokemonlist.R
import com.nehak.pokemonlist.backend.models.PokemonModel
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
    lateinit var activity: PokemonListActivity;

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
    }

    @Test
    fun checkErrorSnackBarVisibility() {
        activity.viewModel.setErrorMessage("Error")
        onView(withText("Error")).check(matches(isDisplayed()))
    }

    @Test
    fun checkCenterErrorViewVisibility() {
        activity.viewModel.setErrorMessage("Error")
        activity.viewModel.clearPokemonList()
        isVisible()

        activity.viewModel.setPokemonList(ArrayList())
        isVisible()

        activity.viewModel.setPokemonList(mockPokemonList(1))
        isGone()
    }

    fun mockPokemon() = PokemonModel(
        name = "Pokemon Name",
        url = "https://pokeapi.co/api/v2/pokemon/45/"
    )

    fun mockPokemonList(size: Int): ArrayList<PokemonModel> {
        val mockModels = ArrayList<PokemonModel>()
        for (i in 1..size) {
            mockModels.add(mockPokemon());
        }
        return mockModels;
    }

}