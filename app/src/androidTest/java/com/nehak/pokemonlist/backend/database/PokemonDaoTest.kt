package com.nehak.pokemonlist.backend.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nehak.pokemonlist.util.MockUtilAndroidTest.mockPokemon
import com.nehak.pokemonlist.util.MockUtilAndroidTest.mockPokemonList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by Neha Kushwah on 7/9/21.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class PokemonDaoTest {
    private lateinit var db: PokemonDatabase
    private lateinit var pokemonDao: PokemonDao

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, PokemonDatabase::class.java
        ).build()
        pokemonDao = db.pokemonDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndLoadPokemonListTest() = runBlocking {

        val mockDataList = mockPokemonList(1)
        pokemonDao.insertPokemonList(mockDataList)

        val loadFromDB = pokemonDao.getPokemonList()
        MatcherAssert.assertThat(loadFromDB.toString(), `is`(mockDataList.toString()))

        val mockData = mockPokemon()
        MatcherAssert.assertThat(loadFromDB[0].toString(), `is`(mockData.toString()))
    }
}