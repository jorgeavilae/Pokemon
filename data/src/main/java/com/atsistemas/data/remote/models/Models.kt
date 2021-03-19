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
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// Data classes para mapear el esquema JSON

/* Holder de la lista de pokemons */
@JsonClass(generateAdapter = true)
data class Generation (
    @Json(name = "pokemon_species") val pokemons: List<Pokemon>
)

fun Pokemon.toPokemonDTO() = PokemonDTO(
    name = this.name,
    order = this.order ?: 0,
    height = this.height ?: 0,
    weight = this.weight ?: 0,
    imgUrlOfficial = this.sprites?.other?.official?.url ?: "",
    imgUrlMini = this.sprites?.front ?: "",
    imgUrlPaint = this.sprites?.other?.dream?.url ?: ""
)

/* Pokemon object */
@JsonClass(generateAdapter = true)
data class Pokemon (
    val name: String,
    val order: Int?,
    val height: Int?,
    val weight: Int?,
    val sprites: Sprites?
)

/* Todas las URL de imágenes de un Pokemon se guardan bajo la etiqueta "sprites"
*
  "sprites":{
    "back_default":"URL_completa",
    "back_female":null,
    "back_shiny":"URL_completa",
    "back_shiny_female":null,
    "front_default":"URL_completa",
    "front_female":null,
    "front_shiny":"URL_completa",
    "front_shiny_female":null,
    "other":{
      "dream_world":{
        "front_default":"URL_completa",
        "front_female":null
      },
      "official-artwork":{
        "front_default":"URL_completa"
      }
    },
    "versions":{
      "generation-i":{
        "red-blue":{
          "back_default":"URL_completa",
          "back_gray":"URL_completa",
          "front_default":"URL_completa",
          "front_gray":"URL_completa"
        },
        ...
      },
      ...
    }
  },
*
* */
@JsonClass(generateAdapter = true)
data class Sprites(
    @Json(name= "front_default") val front: String?,
    val other: OtherSprites
)
@JsonClass(generateAdapter = true)
data class OtherSprites(
    @Json(name= "dream_world") val dream: FrontImageUrl,
    @Json(name= "official-artwork") val official: FrontImageUrl
)
@JsonClass(generateAdapter = true)
data class FrontImageUrl(
    @Json(name= "front_default") val url: String?
)