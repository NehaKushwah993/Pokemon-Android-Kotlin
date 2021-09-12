package com.nehak.pokemonlist.backend.repository

import com.nehak.pokemonlist.backend.dataSource.PokemonRemoteDataSource
import com.nehak.pokemonlist.backend.database.PokemonDao
import com.nehak.pokemonlist.backend.models.pokemonList.PokemonListResponse
import com.nehak.pokemonlist.backend.network.PokemonService
import com.nehak.pokemonlist.backend.other.ApiResult
import com.nehak.pokemonlist.util.MockUtil
import com.nehak.pokemonlist.util.MockUtil.mockPokemonList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

/**
 * Created by Neha Kushwah on 8/9/21.
 * Repository which provides fake data for testing.
 */
class PokemonRepositoryTest {

    private lateinit var pokemonRepository: PokemonRepository
    private var pokemonService: PokemonService = mock(PokemonService::class.java)
    private var pokemonDao: PokemonDao = mock(PokemonDao::class.java)

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
        Mockito.`when`(pokemonDao.getPokemonListForPage(0)).thenReturn(mockData.results)
        Mockito.`when`(pokemonService.fetchPokemonList(0,10)).thenReturn(ApiResult.success(mockData))

        val pokemonList = pokemonRepository.fetchPokemonList(
            pageNumber = 0,
            limit = 10,
            onStart = {},
            onComplete = {},
            onError = {}
        ).first()

        val expectItem = pokemonList[0]
        Assert.assertEquals(expectItem.name,  MockUtil.mockPokemon().name)
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
        Mockito.`when`(pokemonDao.getPokemonListForPage(0)).thenReturn(mockData.results)

        val pokemonList = pokemonRepository.fetchPokemonList(
            pageNumber = 0,
            limit = 1,
            onStart = {},
            onComplete = {},
            onError = {}
        ).first()

        val expectItem = pokemonList[0]
        Assert.assertEquals(expectItem.name,  MockUtil.mockPokemon().name)
        Assert.assertEquals(expectItem, mockPokemonList(1)[0])
    }

    @Test
    fun `test listOfPokemon when no internet or error from service + no data on db`() =
        runBlocking {
            Mockito.`when`(pokemonDao.getPokemonList()).thenReturn(mockPokemonList(0))
            Mockito.`when`(pokemonDao.getPokemonListForPage(0)).thenReturn(mockPokemonList(0))
            Mockito.`when`(pokemonService.fetchPokemonList(0,10))
                .thenReturn(ApiResult.error("Error message"))
            

            val listOfPokemonList = pokemonRepository.fetchPokemonList(
                pageNumber = 0,
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
        Mockito.`when`(pokemonDao.getPokemonListForPage(0)).thenReturn(mockData.results)
        Mockito.`when`(pokemonService.fetchPokemonList(0,10))
            .thenReturn(ApiResult.error("Error message"))

        val pokemonList = pokemonRepository.fetchPokemonList(
            pageNumber = 0,
            limit = 10,
            onStart = {},
            onComplete = {},
            onError = {}
        ).first()

        val expectItem = pokemonList[0]
        Assert.assertEquals(expectItem.name,  MockUtil.mockPokemon().name)
        Assert.assertEquals(expectItem, mockPokemonList(1)[0])
    }


}