/*
 *    Copyright 2021 Jorge Ávila Estévez
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.atsistemas.data.di.providers

import android.app.Application
import android.util.Log
import com.atsistemas.data.BuildConfig
import com.atsistemas.data.local.PokemonDatabase
import com.atsistemas.data.remote.IPokemonAPI
import com.atsistemas.data.remote.interceptors.MockInterceptor
import com.atsistemas.data.remote.models.StatsAdapter
import com.atsistemas.data.repositories.PokemonRepository
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


fun provideMockInterceptor(application: Application): Interceptor =
    MockInterceptor(application)

fun provideOkHttpClient(mockInterceptor: Interceptor?): OkHttpClient {
    val client = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)

    // If debug version: show logging messages
    if (BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor {
            Log.d("HttpLoggingInterceptor", it)
        }.setLevel(HttpLoggingInterceptor.Level.BASIC)
        client.addInterceptor(logging)
    }

    // If mock flavour: intercept retrofit returns
    if (BuildConfig.mock)
        mockInterceptor?.let {
            client.addInterceptor(it)
        }

    return client.build()
}

fun provideMoshi(): Moshi =
    Moshi.Builder()
        .add(StatsAdapter())
        .build()

fun provideRetrofit(httpClient: OkHttpClient, moshi: Moshi): Retrofit =
    Retrofit.Builder()
        .client(httpClient)
        .baseUrl(BuildConfig.BaseURL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

fun providePokemonApi(retrofit: Retrofit): IPokemonAPI =
    retrofit.create(IPokemonAPI::class.java)

fun providePokemonDatabase(application: Application): PokemonDatabase =
    PokemonDatabase.getInstance(application)

fun providePokemonRepository(
    application: Application,
    retrofit: IPokemonAPI,
    pokemonDatabase: PokemonDatabase
): PokemonRepository =
    PokemonRepository(application, retrofit, pokemonDatabase)