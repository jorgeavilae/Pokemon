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

package com.atsistemas.data.utils

import com.atsistemas.data.commons.Constants
import com.atsistemas.data.models.PokemonDTO
import com.atsistemas.data.remote.models.Pokemon
import java.util.*

object PokemonDTOUtils {

    // Transforma un objeto Pokemon con el formato Json del servidor, en un objeto PokemonDTO
    fun pokemonRemoteToPokemonDTO(pokemon: Pokemon) = PokemonDTO(
        id = pokemon.id,
        name = pokemon.name.capitalize(Locale.getDefault()),
        order = pokemon.order,
        height = pokemon.height / 10f,
        weight = pokemon.weight / 10f,
        specie = pokemon.species.name,
        types = pokemon.types.map { it.type.name },
        imgUrlOfficial = pokemon.sprites.other.official.url ?: "",
        imgUrlMiniFront = pokemon.sprites.front ?: "",
        imgUrlMiniBack = pokemon.sprites.back ?: "",
        imgUrlPaint = pokemon.sprites.other.dream.url ?: "",
        hp = pokemon.stats.hp,
        attack = pokemon.stats.attack,
        defense = pokemon.stats.defense,
        specialAttack = pokemon.stats.specialAttack,
        specialDefense = pokemon.stats.specialDefense,
        speed = pokemon.stats.speed,
    )

    // Convierte la lista de tipos del Pokemon, en una cadena de texto legible y que se puede
    // insertar en la base de datos Room
    fun convertListTypesToString(typesList: List<String>, delimiter: String = Constants.TYPE_SEPARATOR): String =
        typesList.joinToString(separator = delimiter)

    // Convierte la cadena de texto insertada en Room, en una lista de tipos manejable.
    fun convertStringToListTypes(string: String): List<String> =
        string.split(Constants.TYPE_SEPARATOR)
}