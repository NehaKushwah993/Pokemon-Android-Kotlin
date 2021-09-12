package com.nehak.pokemonlist.ui.pokemonSearch

import com.nehak.pokemonlist.backend.dataSource.PokemonRemoteDataSource
import com.nehak.pokemonlist.backend.database.PokemonDao
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.backend.network.PokemonService
import com.nehak.pokemonlist.backend.other.ApiResult
import com.nehak.pokemonlist.backend.repository.PokemonRepository
import com.nehak.pokemonlist.util.MockUtil
import com.nehak.pokemonlist.util.MockUtil.mockPokemonByName
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
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
class PokemonSearchViewModelTest {

    private lateinit var pokemonRepository: PokemonRepository
    private var pokemonDao: PokemonDao = mock(PokemonDao::class.java)
    private var pokemonService: PokemonService = mock(PokemonService::class.java)

    @Before
    fun setup() {
        pokemonRepository = PokemonRepository(PokemonRemoteDataSource(pokemonService), pokemonDao)
    }

    @Test
    fun `test fetchPokemonSearch with 5 items list available only in local , not remote`() {
        runBlocking {
            val limit = 5
            val search = MockUtil.mockPokemon().name
            val mockData = MockUtil.mockPokemonList(limit)
            `when`(pokemonService.fetchPokemonByName(search))
                .thenReturn(ApiResult.error("Error message"))
            `when`(pokemonDao.searchPokemonByName(search)).thenReturn(mockData)

            val fetchedDataFlow = pokemonRepository.searchPokemonList(
                pokemonName = search,
                onStart = {},
                onComplete = {},
                onError = {}
            ).first()

            Assert.assertNotNull("fetchedDataFlow = ", fetchedDataFlow)
            Assert.assertEquals(fetchedDataFlow, mockData)
            Assert.assertEquals(fetchedDataFlow.size, mockData.size)
            Assert.assertEquals(fetchedDataFlow[0].name, search)
        }
    }

    @Test
    fun `test fetchPokemonSearch with 1 pokemon available only in remote , not local`() {
        runBlocking {
            val searchString = MockUtil.mockPokemon().name
            val mockData = MockUtil.mockPokemonList(1)
            val empty = MockUtil.mockPokemonList(0)
            `when`(pokemonService.fetchPokemonByName(searchString))
                .thenReturn(ApiResult.success(mockData[0]))
            `when`(pokemonDao.searchPokemonByName(searchString)).thenReturn(empty)

            val fetchedDataFlow = pokemonRepository.searchPokemonList(
                pokemonName = searchString,
                onStart = {},
                onComplete = {},
                onError = {}
            ).last()

            Assert.assertNotNull("fetchedDataFlow = ", fetchedDataFlow)
            Assert.assertEquals(fetchedDataFlow, mockData)
            Assert.assertEquals(1, fetchedDataFlow.size)
            Assert.assertEquals(fetchedDataFlow[0].name, searchString)
        }
    }

    @Test
    fun `test fetchPokemonSearch with 1 pokemon available only in remote , and 5 in local`() {
        runBlocking {
            val searchString = "pokemon"
            val mockDataLocal = MockUtil.mockPokemonList(5)

            val mockDataRemote = ArrayList<PokemonModel>()
            mockDataRemote.add(mockPokemonByName(searchString))

            `when`(pokemonService.fetchPokemonByName(searchString))
                .thenReturn(ApiResult.success(mockDataRemote[0]))
            `when`(pokemonDao.searchPokemonByName(searchString)).thenReturn(mockDataLocal)

            val fetchedDataFlow = pokemonRepository.searchPokemonList(
                pokemonName = searchString,
                onStart = {},
                onComplete = {},
                onError = {}
            ).last()

            Assert.assertNotNull("fetchedDataFlow = ", fetchedDataFlow)
            Assert.assertEquals(6, fetchedDataFlow.size)
            val lastItem = fetchedDataFlow[fetchedDataFlow.size - 1]
            Assert.assertEquals(searchString, lastItem.name)
        }
    }
    @Test
    fun `test fetchPokemonSearch with 0 pokemon available only in remote , and 0 in local`() {
        runBlocking {

            val searchString = "pokemon"
            val empty = MockUtil.mockPokemonList(0)

            `when`(pokemonService.fetchPokemonByName(searchString))
                .thenReturn(ApiResult.error("Error"))
            `when`(pokemonDao.searchPokemonByName(searchString)).thenReturn(empty)

            val fetchedDataFlow = pokemonRepository.searchPokemonList(
                pokemonName = searchString,
                onStart = {},
                onComplete = {},
                onError = {}
            ).last()

            Assert.assertNotNull("fetchedDataFlow = ", fetchedDataFlow)
            Assert.assertEquals(0, fetchedDataFlow.size)
        }
    }
}