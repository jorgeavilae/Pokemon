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

import com.atsistemas.data.commons.Constants
import com.squareup.moshi.*


/*
* Las estadísticas iniciales se guarda bajo "stats", en una lista, con un "stat" que indica
* el nombre y la url explicativa de la estadística: puntos de vida, ataque, defensa,
* ataque especial, defensa especial y velocidad.
*
  "stats":[
    {
      "base_stat":45,
      "effort":0,
      "stat":{
        "name":"hp",
        "url":"https://pokeapi.co/api/v2/stat/1/"
      }
    },
    {
      "base_stat":49,
      "effort":0,
      "stat":{
        "name":"attack",
        "url":"https://pokeapi.co/api/v2/stat/2/"
      }
    },
    ....
 * */

data class FullStats(
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int
)

@JsonClass(generateAdapter = true)
data class Stat(
    @Json(name = "base_stat") val value: Int,
    @Json(name = "stat") val statName: StatName
)

@JsonClass(generateAdapter = true)
data class StatName(
    val name: String
)

// Adapter para convertir data classes del esquema JSON (Stat y StatName)
// en una clase con las 5 stats (FullStats), y viceversa
class StatsAdapter {

    // Convierte FullStats en una lista de stats tal y como se especifica en el esquema JSON del servidor.
    @ToJson
    fun toJson(fullStats: FullStats): List<Stat> {
        return listOf(
            Stat(fullStats.hp, StatName(Constants.REMOTE_JSON_STAT_HP)),
            Stat(fullStats.attack, StatName(Constants.REMOTE_JSON_STAT_ATTACK)),
            Stat(fullStats.defense, StatName(Constants.REMOTE_JSON_STAT_DEFENSE)),
            Stat(fullStats.specialAttack, StatName(Constants.REMOTE_JSON_STAT_SPECIAL_ATTACK)),
            Stat(fullStats.specialDefense, StatName(Constants.REMOTE_JSON_STAT_SPECIAL_DEFENSE)),
            Stat(fullStats.speed, StatName(Constants.REMOTE_JSON_STAT_SPEED)),
        )
    }

    // Convierte una lista de stats con el esquema del servidor en un FullStats con las 5 stats principales
    @FromJson
    fun fromJson(stats: List<Stat>): FullStats {
        var hp = 0
        var attack = 0
        var defense = 0
        var specialAttack = 0
        var specialDefense = 0
        var speed = 0

        stats.forEach {
            when (it.statName.name) {
                Constants.REMOTE_JSON_STAT_HP -> hp = it.value
                Constants.REMOTE_JSON_STAT_ATTACK -> attack = it.value
                Constants.REMOTE_JSON_STAT_DEFENSE -> defense = it.value
                Constants.REMOTE_JSON_STAT_SPECIAL_ATTACK -> specialAttack = it.value
                Constants.REMOTE_JSON_STAT_SPECIAL_DEFENSE -> specialDefense = it.value
                Constants.REMOTE_JSON_STAT_SPEED -> speed = it.value
                else -> throw JsonDataException("unknown stat: ${it.statName.name}")
            }
        }

        return FullStats(hp, attack, defense, specialAttack, specialDefense, speed)
    }
}