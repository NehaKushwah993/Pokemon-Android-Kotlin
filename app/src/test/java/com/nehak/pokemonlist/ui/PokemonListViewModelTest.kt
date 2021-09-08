package com.nehak.pokemonlist.ui

import com.nehak.pokemonlist.backend.database.PokemonDao
import com.nehak.pokemonlist.repository.FakePokemonRepository
import com.nehak.pokemonlist.util.MockUtil
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Created by Neha Kushwah on 8/9/21.
 * PokemonListViewModelTest to test its methods
 */
class PokemonListViewModelTest {

    private lateinit var viewModel: PokemonListViewModel;
    private lateinit var pokemonRepository: FakePokemonRepository
    private var pokemonDao: PokemonDao = mock(PokemonDao::class.java)

    @Before
    fun setup() {
        pokemonRepository = FakePokemonRepository()
    }

    @Test
    fun `test fetchPokemonList with 5 elements`() {
        runBlocking {
            var limit = 5;
            val mockData = MockUtil.mockPokemonList(limit)
            `when`(pokemonDao.getPokemonList()).thenReturn(mockData)

            val fetchedDataFlow = pokemonRepository.fetchPokemonList(
                limit = limit,
                onStart = {},
                onComplete = {},
                onError = {}
            ).first();

            Assert.assertNotNull("fetchedDataFlow = ", fetchedDataFlow)
            Assert.assertEquals(fetchedDataFlow, MockUtil.mockPokemonList(limit))
            Assert.assertEquals(fetchedDataFlow[0].name, "Pokemon Name")
        }
    }

    @Test
    fun `test fetchPokemonList with 0 elements`() {
        runBlocking {
            var limit = 0;
            val mockData = MockUtil.mockPokemonList(limit)
            `when`(pokemonDao.getPokemonList()).thenReturn(mockData)

            val fetchedDataFlow = pokemonRepository.fetchPokemonList(
                limit = limit,
                onStart = {},
                onComplete = {},
                onError = {}
            ).first();

            Assert.assertNotNull("fetchedDataFlow = ", fetchedDataFlow)
            Assert.assertEquals(fetchedDataFlow, MockUtil.mockPokemonList(limit))
            Assert.assertEquals(fetchedDataFlow.size, MockUtil.mockPokemonList(limit).size)
            Assert.assertEquals(fetchedDataFlow.size, limit)
            Assert.assertNotEquals(fetchedDataFlow.size, 1)

        }

    }
}