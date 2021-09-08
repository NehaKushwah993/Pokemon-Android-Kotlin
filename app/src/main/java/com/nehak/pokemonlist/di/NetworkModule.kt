package com.nehak.pokemonlist.di

import com.nehak.pokemonlist.BuildConfig
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by Neha Kushwah on 7/9/21.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val baseUrl = BuildConfig.BASE_URL

}