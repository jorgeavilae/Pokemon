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

package com.atsistemas.pokemon.main_activity.list.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atsistemas.data.models.PokemonDTO
import com.atsistemas.data.remote.ResultHandler
import com.atsistemas.data.repositories.PokemonRepository
import com.atsistemas.pokemon.commons.BaseViewModel
import com.atsistemas.pokemon.commons.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(private val repository: PokemonRepository) : BaseViewModel() {

    // MediatorLiveData permite fusionar LiveDatas reaccionando a los cambios en todos (ver init{})
    // La lista de MediatorLiveData alojará:
    //  - Si no hay filtro, todos los pokemons
    //  - Si sí hay filtro, una lista de pokemons cuyo nombre coincide con el filtro.
    val pokemons: MediatorLiveData<List<PokemonDTO>> = MediatorLiveData()

    // Todos los pokemons guardados en local (en Room)
    private val _allPokemons: LiveData<List<PokemonDTO>> = repository.pokemons

    // String para búsquedas que sirve como filtro
    private val _filter: MutableLiveData<String> = MutableLiveData("")


    init {
        // MediatorLiveData reacciona a los cambios en ambos LiveData.
        // Si cambia la lista completa de pokemons y no hay filtro, se muestra la lista completa.
        pokemons.addSource(_allPokemons) {
            if (_filter.value.isNullOrBlank())
                pokemons.value = it
        }
        // Si cambia el filtro y es válido, se realiza la búsqueda en el Repository.
        pokemons.addSource(_filter) {
            if (_filter.value.isNullOrBlank())
                pokemons.value = _allPokemons.value
            else
                viewModelScope.launch(Dispatchers.IO) {
                    pokemons.postValue(repository.searchPokemons(it))
                }
        }
    }

    fun fetchData() {
        setShowLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            val range = 1..Constants.MAX_POKEMON_LIST
            when (val result = repository.loadPokemonByIdRangeFromServer(range)) {
                is ResultHandler.Success -> {
                    setShowMessage(result.data)
                }
                else -> {
                    setShowError(result)
                }
            }
            setShowLoading(false)
        }
    }

    fun setFiler(filter: String?) {
        _filter.value = filter
    }

    fun deleteDB() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDB()
        }
    }
}