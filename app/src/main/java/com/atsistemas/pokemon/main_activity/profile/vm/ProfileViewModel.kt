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
import androidx.lifecycle.viewModelScope
import com.atsistemas.data.repositories.PokemonRepository
import com.atsistemas.pokemon.commons.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: PokemonRepository) : BaseViewModel() {

    // Nombre del personaje
    val name: LiveData<String> = repository.preferencesName

//    // Tiempo jugado
//    private val _time = MutableLiveData<Long>()
//    val time: LiveData<Long>
//        get() = _time
//
//    // Medallas de gimnasio obtenidas
//    private val _badges = MutableLiveData<Int>()
//    val badges: LiveData<Int>
//        get() = _badges
//
//    // Pokemons de la Pokedex avistados
//    private val _pokedex = MutableLiveData<Int>()
//    val pokedex: LiveData<Int>
//        get() = _pokedex

    fun setName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setName(name)
        }
    }

//    fun setTime(time: Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.setTime(time)
//        }
//    }
//
//    fun setBadges(badges: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.setBadges(badges)
//        }
//    }
//
//    fun setPokedex(pokedex: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.setPokedex(pokedex)
//        }
//    }
}