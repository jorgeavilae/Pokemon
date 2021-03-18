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

import com.atsistemas.data.commons.BaseRepository
import com.atsistemas.data.models.Pokemon
import com.atsistemas.data.remote.IPokemonAPI
import com.atsistemas.data.remote.ResultHandler

class PokemonRepository(private val api: IPokemonAPI) : BaseRepository() {

    private var pokemons: List<Pokemon> = emptyList()
    private var pokemon: Pokemon? = null

    suspend fun getListData(): List<Pokemon> {
        getGeneration(1)
        return pokemons
    }

    suspend fun getProfileData(): Pokemon? {
        getPokemon("bulbasaur")
        return pokemon
    }



    //API
    suspend fun getGeneration(generationId: Int): ResultHandler<String> {
        return when (val result = safeApiCall { api.getGeneration(generationId) }) {
            is ResultHandler.Success -> {
                result.data.let {
                    pokemons = it.pokemons
                    // todo save to local db
                }
                ResultHandler.Success("Successful update")
            }
            is ResultHandler.GenericError -> result
            is ResultHandler.HttpError -> result
            is ResultHandler.NetworkError -> result
        }
    }

    suspend fun getPokemon(name: String): ResultHandler<String> {
        return when (val result = safeApiCall { api.getPokemon(name) }) {
            is ResultHandler.Success -> {
                result.data.let {
                    pokemon = it
                    // todo save to local db
                }
                ResultHandler.Success("Successful update")
            }
            is ResultHandler.GenericError -> result
            is ResultHandler.HttpError -> result
            is ResultHandler.NetworkError -> result
        }
    }
}