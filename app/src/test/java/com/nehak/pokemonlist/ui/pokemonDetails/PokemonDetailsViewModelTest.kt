package com.nehak.pokemonlist.ui.pokemonDetails

import com.nehak.pokemonlist.backend.dataSource.PokemonRemoteDataSource
import com.nehak.pokemonlist.backend.database.PokemonDao
import com.nehak.pokemonlist.backend.network.PokemonService
import com.nehak.pokemonlist.backend.repository.PokemonRepository
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
class PokemonDetailsViewModelTest {

    private lateinit var pokemonRepository: PokemonRepository
    private var pokemonDao: PokemonDao = mock(PokemonDao::class.java)
    private var pokemonService: PokemonService = mock(PokemonService::class.java)

    @Before
    fun setup() {
        pokemonRepository = PokemonRepository(PokemonRemoteDataSource(pokemonService), pokemonDao)
    }

    @Test
    fun `test fetchPokemonDetails`() {
        runBlocking {
            val mockData = MockUtil.mockPokemonDetail()
            `when`(pokemonDao.getPokemonDetails(name_ = mockData.name)).thenReturn(mockData)

            val fetchedDataFlow = pokemonRepository.fetchPokemonDetail(
                pokemonName = mockData.name,
                onStart = { },
                onComplete = { },
                onError = { }
            ).first()

            Assert.assertNotNull("fetchedDataFlow = ", fetchedDataFlow)
            Assert.assertEquals(pokemonDao.getPokemonDetails(name_ = mockData.name), fetchedDataFlow)
        }
    }
}