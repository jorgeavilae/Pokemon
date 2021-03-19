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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atsistemas.data.models.PokemonDTO
import com.atsistemas.data.repositories.PokemonRepository
import com.atsistemas.pokemon.commons.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: PokemonRepository) : BaseViewModel() {

    private val _pokemon = MutableLiveData<PokemonDTO>()
    val pokemon: LiveData<PokemonDTO>
        get() = _pokemon

    fun fetchData() {
        viewModelScope.launch (Dispatchers.IO) {
            _pokemon.postValue(repository.getProfileData())
        }
    }
}