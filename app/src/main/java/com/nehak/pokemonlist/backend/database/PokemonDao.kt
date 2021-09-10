package com.nehak.pokemonlist.backend.database

import androidx.room.*
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.backend.models.pokemonDetails.PokemonDetails

/**
 * Created by Neha Kushwah on 7/9/21.
 */
@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemonList: List<PokemonModel>)

    @Query("SELECT * FROM PokemonModel WHERE pageNumber = :pageNumber")
    suspend fun getPokemonListForPage(pageNumber: Int): List<PokemonModel>

    @Query("SELECT * FROM PokemonModel")
    suspend fun getPokemonList(): List<PokemonModel>

    @Query("DELETE FROM PokemonModel")
    fun deleteAll(): Int

    @Query("SELECT * FROM PokemonDetails WHERE name = :name_")
    suspend fun getPokemonDetails(name_: String): PokemonDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetails(pokemonDetails: PokemonDetails)
}
