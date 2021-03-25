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

import androidx.lifecycle.LiveData
import com.atsistemas.data.commons.BaseRepository
import com.atsistemas.data.local.PokemonDatabase
import com.atsistemas.data.local.preferences.ProfilePreferencesWrapper
import com.atsistemas.data.models.PokemonDTO
import com.atsistemas.data.remote.IPokemonAPI
import com.atsistemas.data.remote.ResultHandler
import com.atsistemas.data.utils.PokemonDTOUtils

class PokemonRepository(
    private val api: IPokemonAPI,
    private val pokemonDatabase: PokemonDatabase,
    private val profilePreferences: ProfilePreferencesWrapper
) : BaseRepository() {

    // Room
    val pokemons: LiveData<List<PokemonDTO>> by lazy {
        pokemonDatabase.pokemonDao().load()
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

    // DATASTORE
    val preferencesName: LiveData<String> by lazy { profilePreferences.preferencesName }
    val preferencesTime: LiveData<String> by lazy { profilePreferences.preferencesTime }
    val preferencesBadges: LiveData<String> by lazy { profilePreferences.preferencesBadges }
    val preferencesPokedex: LiveData<String> by lazy { profilePreferences.preferencesPokedex }

    suspend fun setName(name: String) = profilePreferences.setName(name)
    suspend fun setTime(time: String) = profilePreferences.setTime(time)
    suspend fun setBadges(badges: String) = profilePreferences.setBadges(badges)
    suspend fun setPokedex(pokedex: String) = profilePreferences.setPokedex(pokedex)
}