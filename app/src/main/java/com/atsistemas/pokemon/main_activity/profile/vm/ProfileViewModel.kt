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
import com.atsistemas.pokemon.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: PokemonRepository) : BaseViewModel() {

    val name: LiveData<String> = repository.preferencesName
    val badges: LiveData<Int> = repository.preferencesBadges
    val time: LiveData<String> = repository.preferencesTime
    val pokedex: LiveData<String> = repository.preferencesPokedex

    val nameEvent = SingleLiveEvent<String>()
    val badgesEvent = SingleLiveEvent<Int>()
    val timeEvent = SingleLiveEvent<String>()
    val pokedexEvent = SingleLiveEvent<String>()

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            nameEvent.postValue(repository.getName())
            badgesEvent.postValue(repository.getBadges())
            timeEvent.postValue(repository.getTime())
            pokedexEvent.postValue(repository.getPokedex())
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

    fun setTime(time: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setTime(time)
        }
    }

    fun setPokedex(pokedex: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setPokedex(pokedex)
        }
    }
}