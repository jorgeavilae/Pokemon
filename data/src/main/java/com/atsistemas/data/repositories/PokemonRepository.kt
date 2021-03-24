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

package com.atsistemas.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.atsistemas.data.commons.BaseRepository
import com.atsistemas.data.commons.Constants
import com.atsistemas.data.local.PokemonDatabase
import com.atsistemas.data.models.PokemonDTO
import com.atsistemas.data.remote.IPokemonAPI
import com.atsistemas.data.remote.ResultHandler
import com.atsistemas.data.utils.PokemonDTOUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.DATASTORE_NAME)

class PokemonRepository(
    private val context: Context,
    private val api: IPokemonAPI,
    private val pokemonDatabase: PokemonDatabase
) : BaseRepository() {

    // Room
    val pokemons: LiveData<List<PokemonDTO>> by lazy {
        pokemonDatabase.pokemonDao().load()
    }

    //todo cambiar
    fun getProfileData(): PokemonDTO? {
        return pokemonDatabase.pokemonDao().getPokemonById(1)
    }

    // Retrofit API
    @Suppress("unused")
    suspend fun loadGenerationFromServer(generationId: Int): ResultHandler<String> {
        return when (val result = safeApiCall { api.getGeneration(generationId) }) {
            is ResultHandler.Success -> {
                result.data.pokemons.forEach {
                    val resultPokemon = loadPokemonFromServer(it.name)
                    if (resultPokemon !is ResultHandler.Success)
                        return resultPokemon
                }
                ResultHandler.Success("Successful update")
            }
            is ResultHandler.GenericError -> result
            is ResultHandler.HttpError -> result
            is ResultHandler.NetworkError -> result
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun loadPokemonFromServer(name: String): ResultHandler<String> {
        return when (val result = safeApiCall { api.getPokemonByName(name) }) {
            is ResultHandler.Success -> {
                pokemonDatabase.pokemonDao().save(
                    PokemonDTOUtils.pokemonRemoteToPokemonDTO(result.data)
                )
                ResultHandler.Success("Successful update")
            }
            is ResultHandler.GenericError -> result
            is ResultHandler.HttpError -> result
            is ResultHandler.NetworkError -> result
        }
    }

    suspend fun loadPokemonByIdRangeFromServer(range: IntRange): ResultHandler<String> {
        for (id in range) {
            val resultPokemon = when (val result = safeApiCall { api.getPokemonById(id) }) {
                is ResultHandler.Success -> {
                    pokemonDatabase.pokemonDao().save(
                        PokemonDTOUtils.pokemonRemoteToPokemonDTO(result.data)
                    )
                    ResultHandler.Success("Successful update")
                }
                is ResultHandler.GenericError -> result
                is ResultHandler.HttpError -> result
                is ResultHandler.NetworkError -> result
            }
            if (resultPokemon !is ResultHandler.Success)
                return resultPokemon
        }
        return ResultHandler.Success("Successful update")
    }

    // DataStore
    val preferencesName: LiveData<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            val nameKey = stringPreferencesKey(Constants.PREFERENCES_NAME_KEY)
            preferences[nameKey] ?: ""
        }.asLiveData()

    suspend fun setName(name: String) {
        context.dataStore.edit { preferences ->
            val nameKey = stringPreferencesKey(Constants.PREFERENCES_NAME_KEY)
            preferences[nameKey] = name
        }
    }
}