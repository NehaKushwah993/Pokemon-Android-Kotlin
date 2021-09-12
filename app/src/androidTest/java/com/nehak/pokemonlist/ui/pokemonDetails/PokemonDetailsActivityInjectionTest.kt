package com.nehak.pokemonlist.ui.pokemonDetails

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nehak.pokemonlist.util.MockUtilAndroidTest
import com.nehak.pokemonlist.utils.EXTRA_POKEMON
import junit.framework.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PokemonDetailsActivityInjectionTest {

    @Test
    fun verifyInjection() {

        val intent = Intent(ApplicationProvider.getApplicationContext(), PokemonDetailsActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(EXTRA_POKEMON, MockUtilAndroidTest.mockPokemon())
        intent.putExtras(bundle)
        ActivityScenario.launch<PokemonDetailsActivity>(intent).use {
            it.moveToState(Lifecycle.State.CREATED)
            it.onActivity { activity ->
                assertNotNull(activity.viewModelFactory)
                assertNotNull(activity.viewModel)
            }
        }
    }

}