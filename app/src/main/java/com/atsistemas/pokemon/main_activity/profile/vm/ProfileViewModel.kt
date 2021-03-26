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

package com.atsistemas.pokemon.main_activity.profile.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.atsistemas.data.repositories.PokemonRepository
import com.atsistemas.pokemon.commons.BaseViewModel
import com.atsistemas.pokemon.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("MemberVisibilityCanBePrivate")
class ProfileViewModel(private val repository: PokemonRepository) : BaseViewModel() {

    // Nombre del jugador (en DataStore)
    val name: LiveData<String> = repository.preferencesName

    // Medallas "conseguidas" por el jugador (en DataStore)
    val badges: LiveData<Int> = repository.preferencesBadges

    // MediatorLiveData permite fusionar LiveDatas reaccionando a los cambios en todos (ver init{})
    val pokedex: MediatorLiveData<String> = MediatorLiveData()
    // Pokemon "capturados" (en DataStore)
    val pokedexCount: LiveData<Int> = repository.preferencesPokedex
    // Pokemons en la Pokedex (Room rows count)
    val maxPokedex: LiveData<Int> = repository.pokemonCount

    // Tiempo jugado (en DataStore)
    val time: LiveData<String> = repository.preferencesTime

    // Single Events para inicializar las Vistas de entrada de datos
    val nameEvent = SingleLiveEvent<String>()
    val badgesEvent = SingleLiveEvent<Int>()
    val pokedexEvent = SingleLiveEvent<Int>()
    val timeEvent = SingleLiveEvent<String>()

    init {
        // Asigna las fuentes de LiveData con las que se construirá el String de MediatorLiveData
        pokedex.addSource(pokedexCount) {
            pokedex.value = "$it / ${maxPokedex.value}"
        }
        pokedex.addSource(maxPokedex) {
            pokedex.value = "${pokedexCount.value} / $it"
        }
    }

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            nameEvent.postValue(repository.getName())
            badgesEvent.postValue(repository.getBadges())
            pokedexEvent.postValue(repository.getPokedex())
            timeEvent.postValue(repository.getTime())
        }
    }

    fun setName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setName(name)
        }
    }

    fun setBadges(badges: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setBadges(badges)
        }
    }

    fun setPokedex(pokedex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setPokedex(pokedex)
        }
    }

    fun setTime(time: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setTime(time)
        }
    }
}