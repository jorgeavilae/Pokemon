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

package com.atsistemas.data.remote.models

import com.atsistemas.data.models.PokemonDTO
import com.squareup.moshi.JsonClass
import java.util.*


/* Pokemon object from server */
@JsonClass(generateAdapter = true)
data class Pokemon(
    val id: Int,
    val name: String,
    val order: Int,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val species: Species,
    val types: List<Tipo>,
    val stats: FullStats
)

fun Pokemon.toPokemonDTO() = PokemonDTO(
    id = this.id,
    name = this.name.capitalize(Locale.getDefault()),
    order = this.order,
    height = this.height,
    weight = this.weight,
    specie = this.species.name,
    type = this.types.joinToString(separator = " - ") { it.type.name },
    imgUrlOfficial = this.sprites.other.official.url ?: "",
    imgUrlMiniFront = this.sprites.front ?: "",
    imgUrlMiniBack = this.sprites.back ?: "",
    imgUrlPaint = this.sprites.other.dream.url ?: "",
    hp = this.stats.hp,
    attack = this.stats.attack,
    defense = this.stats.defense,
    specialAttack = this.stats.specialAttack,
    specialDefense = this.stats.specialDefense,
    speed = this.stats.speed,
)
