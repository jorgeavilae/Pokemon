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

package com.atsistemas.data.commons

object Constants {
    const val DATABASE_NAME = "pokemon_database"
    const val TABLE_POKEMON = "pokemon_table"

    const val REMOTE_JSON_STAT_HP = "hp"
    const val REMOTE_JSON_STAT_ATTACK = "attack"
    const val REMOTE_JSON_STAT_DEFENSE = "defense"
    const val REMOTE_JSON_STAT_SPECIAL_ATTACK = "special-attack"
    const val REMOTE_JSON_STAT_SPECIAL_DEFENSE = "special-defense"
    const val REMOTE_JSON_STAT_SPEED = "speed"

    const val TYPE_SEPARATOR = "-"

    // Preferences keys
    const val DATASTORE_NAME = "datastore_pokemon"
    const val PREFERENCES_NAME_KEY = "preferences_name_key"
    const val PREFERENCES_TIME_KEY = "preferences_time_key"
    const val PREFERENCES_BADGES_KEY = "preferences_badges_key"
    const val PREFERENCES_POKEDEX_KEY = "preferences_pokedex_key"
}