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
    val pokedexTotal: LiveData<Int> = repository.pokemonCount

    // Tiempo jugado (en DataStore)
    val time: LiveData<String> = repository.preferencesTime

    // Single Events para inicializar las Vistas de entrada de datos
    val nameEvent = SingleLiveEvent<String>()
    val badgesEvent = SingleLiveEvent<Int>()
    val pokedexEvent = SingleLiveEvent<Int>()
    val hoursEvent = SingleLiveEvent<Int>()
    val minutesEvent = SingleLiveEvent<Int>()

    init {
        // Asigna las fuentes de LiveData con las que se construirá el String de MediatorLiveData
        pokedex.addSource(pokedexCount) {
            pokedex.value = mergePokedexCountTotalIntoString(it, pokedexTotal.value)
        }
        pokedex.addSource(pokedexTotal) {
            pokedex.value = mergePokedexCountTotalIntoString(pokedexCount.value, it)
        }
    }

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            nameEvent.postValue(repository.getName())
            badgesEvent.postValue(repository.getBadges())
            pokedexEvent.postValue(repository.getPokedex())
            hoursEvent.postValue(extractHoursFromString(repository.getTime()) ?: 0)
            minutesEvent.postValue(extractMinutesFromString(repository.getTime()) ?: 0)
        }
    }

    fun deletePreferences() {
        viewModelScope.launch (Dispatchers.IO) {
            repository.deletePreferences()
            fetchData()
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

    fun setTime(hours: Int?, minutes: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setTime(mergeHoursMinutesIntoString(hours, minutes))
        }
    }

    // Converters
    fun mergePokedexCountTotalIntoString(count: Int?, total: Int?): String =
        if (count != null && total != null)
            "$count / $total"
        else
            ""

    fun mergeHoursMinutesIntoString(hours: Int?, minutes: Int?): String =
        if (hours != null && minutes != null)
            "$hours:${minutes.toString().padStart(2, '0')}"
        else
            ""

    fun extractHoursFromString(time: String?): Int? {
        val numberStr = time?.split(":")?.getOrNull(0)
        return if (!numberStr.isNullOrEmpty()) numberStr.toIntOrNull() else null
    }

    fun extractMinutesFromString(time: String?): Int? {
        val numberStr = time?.split(":")?.getOrNull(1)
        return if (!numberStr.isNullOrEmpty()) numberStr.toIntOrNull() else null
    }

}