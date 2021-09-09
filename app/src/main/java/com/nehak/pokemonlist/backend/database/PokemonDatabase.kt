package com.nehak.pokemonlist.backend.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.backend.models.pokemonDetails.PokemonDetails

/**
 * Created by Neha Kushwah on 7/9/21.
 * Database to store data in RoomDB
 */
@Database(entities = [PokemonModel::class, PokemonDetails::class], version = 1, exportSchema = true)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
