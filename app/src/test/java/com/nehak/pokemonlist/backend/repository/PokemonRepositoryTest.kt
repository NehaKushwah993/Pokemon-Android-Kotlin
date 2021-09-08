package com.nehak.pokemonlist.backend.repository

import com.nehak.pokemonlist.backend.dataSource.PokemonRemoteDataSource
import com.nehak.pokemonlist.backend.database.PokemonDao
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.backend.models.pokemonList.PokemonListResponse
import com.nehak.pokemonlist.backend.network.PokemonService
import com.nehak.pokemonlist.backend.other.ApiResult
import com.nehak.pokemonlist.ui.PokemonListViewModel
import com.nehak.pokemonlist.util.MockUtil
import com.nehak.pokemonlist.util.MockUtil.mockPokemonList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import kotlin.time.toDuration

/**
 * Created by Neha Kushwah on 8/9/21.
 * Repository which provides fake data for testing.
 */
class PokemonRepositoryTest() {

    private lateinit var viewModel: PokemonListViewModel;
    private lateinit var pokemonRepository: PokemonRepository
    private var pokemonService: PokemonService = Mockito.mock(PokemonService::class.java)
    private var pokemonDao: PokemonDao = Mockito.mock(PokemonDao::class.java)

    @Before
    fun setup() {
        pokemonRepository = PokemonRepository(PokemonRemoteDataSource(pokemonService), pokemonDao)
    }

    @Test
    fun `test listOfPokemon from service`() = runBlocking {
        val mockData = PokemonListResponse(
            count = 984,
            next = null,
            previous = null,
            results = mockPokemonList(11)
        )
        Mockito.`when`(pokemonDao.getPokemonList()).thenReturn(mockData.results)
        Mockito.`when`(pokemonService.fetchPokemonList(10)).thenReturn(ApiResult.success(mockData))

        var pokemonList = pokemonRepository.fetchPokemonList(
            limit = 10,
            onStart = {},
            onComplete = {},
            onError = {}
        ).first();

        val expectItem = pokemonList[0]
        Assert.assertEquals(expectItem.name, "Pokemon Name")
        Assert.assertEquals(expectItem, mockPokemonList(1)[0])
    }

    @Test
    fun `test listOfPokemon from database`() = runBlocking {
        val mockData = PokemonListResponse(
            count = 984,
            next = null,
            previous = null,
            results = mockPokemonList(1)
        )
        Mockito.`when`(pokemonDao.getPokemonList()).thenReturn(mockData.results)

        var pokemonList = pokemonRepository.fetchPokemonList(
            limit = 1,
            onStart = {},
            onComplete = {},
            onError = {}
        ).first();

        val expectItem = pokemonList[0]
        Assert.assertEquals(expectItem.name, "Pokemon Name")
        Assert.assertEquals(expectItem, mockPokemonList(1)[0])
    }

    @Test
    fun `test listOfPokemon when no internet or error from service + no data on db`() =
        runBlocking {
            Mockito.`when`(pokemonDao.getPokemonList()).thenReturn(MockUtil.mockPokemonList(0))
            Mockito.`when`(pokemonService.fetchPokemonList(10))
                .thenReturn(ApiResult.error("Error message", null))

            var listOfPokemonList = pokemonRepository.fetchPokemonList(
                limit = 10,
                onStart = {},
                onComplete = {},
                onError = {}
            ).toList()

            Assert.assertEquals(listOfPokemonList.size, 0)
        }


    @Test
    fun `test listOfPokemon when no internet or error from service + data on db`() = runBlocking {
        val mockData = PokemonListResponse(
            count = 0,
            next = null,
            previous = null,
            results = mockPokemonList(11)
        )
        Mockito.`when`(pokemonDao.getPokemonList()).thenReturn(mockData.results)
        Mockito.`when`(pokemonService.fetchPokemonList(10))
            .thenReturn(ApiResult.error("Error message", null))

        var pokemonList = pokemonRepository.fetchPokemonList(
            limit = 10,
            onStart = {},
            onComplete = {},
            onError = {}
        ).first();

        val expectItem = pokemonList[0]
        Assert.assertEquals(expectItem.name, "Pokemon Name")
        Assert.assertEquals(expectItem, mockPokemonList(1)[0])
    }


}