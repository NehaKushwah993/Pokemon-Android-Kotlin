package com.nehak.pokemonlist.ui.pokemonDetails

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nehak.pokemonlist.ui.MyActivityScenarioTestRule
import com.nehak.pokemonlist.util.MockUtilAndroidTest
import com.nehak.pokemonlist.utils.EXTRA_POKEMON
import org.hamcrest.Matchers.containsString
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
class PokemonDetailsActivityTest {

    @get:Rule
    var activityRule: MyActivityScenarioTestRule<PokemonDetailsActivity> =
        MyActivityScenarioTestRule()
    private lateinit var activity: PokemonDetailsActivity

    @Before
    fun init() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), PokemonDetailsActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(EXTRA_POKEMON, MockUtilAndroidTest.mockPokemon())
        intent.putExtras(bundle)
        activityRule.launchActivity(intent)
        activityRule.scenario?.moveToState(Lifecycle.State.RESUMED)
        activityRule.scenario?.onActivity {
            activity = it
        }
        Thread.sleep(1000)
    }

    @Test
    fun testPokemonDetailsPage() {
        onView(withId(activity.viewBinding.ivPokemon.id)).check(matches(isDisplayed()))
        onView(withId(activity.viewBinding.tvTitle.id)).check(matches(isDisplayed()))
    }

    @Test
    fun checkErrorSnackBarVisibility() {
        activity.viewModel.setErrorMessage("Unable to fetch data! Please retry!")
        onView(withText("Unable to fetch data! Please retry!")).check(matches(isDisplayed()))
    }

    @Test
    fun checkDataSetOnViews() {

        val pokemon = MockUtilAndroidTest.mockPokemon()
        val pokemonDetail = MockUtilAndroidTest.mockPokemonDetail()

        activity.viewBinding.pokemon = pokemon
        activity.viewBinding.pokemonDetail = pokemonDetail
        onView(withText(pokemon.capitaliseName())).check(
            matches(
                isDisplayed()
            )
        )
        onView(withText(containsString(pokemonDetail.baseExperience.toString()))).check(
            matches(isDisplayed())
        )
        onView(withText(containsString(pokemonDetail.height.toString()))).check(
            matches(isDisplayed())
        )
    }

}