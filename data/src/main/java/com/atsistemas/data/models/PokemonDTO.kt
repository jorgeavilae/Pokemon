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

package com.atsistemas.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.atsistemas.data.commons.Constants
import com.atsistemas.data.utils.PokemonDTOUtils

@Entity(tableName = Constants.TABLE_POKEMON)
data class PokemonDTO(
    @PrimaryKey val id: Int,
    val name: String,
    val order: Int,
    val height: Float,
    val weight: Float,
    val specie: String,
    val types: List<String>,
    val imgUrlOfficial: String,
    val imgUrlMiniFront: String,
    val imgUrlMiniBack: String,
    val imgUrlPaint: String,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int
)

/**
 * Convierte la lista de tipos (Strings) en una sola String, y viceversa, para que pueda ser
 * manejada por Room.
 *
 * Podría hacerse una tabla aparte que relacione cada Pokemon con sus tipos. Pero cada Pokemon
 * tiene sólo 1 o 2 tipos y la String resultante siempre será menor a los 15 caracteres.
 * Creo que es más eficiente guardarlo y consultarlo en una sola tabla con un converter,
 * que usar dos tablas y consultas cruzadas con JOIN.
 */
class PokemonListTypesConverter {
    @TypeConverter
    fun stringFromListTypes(typesList: List<String>?): String? =
        typesList?.let { PokemonDTOUtils.convertListTypesToString(it) }

    @TypeConverter
    fun stringToListTypes(string: String?): List<String>? =
        string?.let { PokemonDTOUtils.convertStringToListTypes(it) }
}
