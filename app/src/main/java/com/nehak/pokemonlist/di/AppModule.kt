package com.nehak.pokemonlist.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nehak.pokemonlist.backend.database.PokemonDao
import com.nehak.pokemonlist.backend.database.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Neha Kushwah on 8/9/21.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesGson(): Gson? {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): PokemonDatabase {
        return Room
            .databaseBuilder(
                appContext,
                PokemonDatabase::class.java,
                appContext.packageName + ".db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePokemonDao(appDatabase: PokemonDatabase): PokemonDao {
        return appDatabase.pokemonDao()
    }
}