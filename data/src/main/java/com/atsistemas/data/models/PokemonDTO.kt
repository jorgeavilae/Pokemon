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
import com.atsistemas.data.commons.Constants

@Entity(tableName = Constants.TABLE_POKEMON)
data class PokemonDTO(
    @PrimaryKey val name: String,
    val order: Int,
    val height: Int,
    val weight: Int,
    val specie: String,
    val imgUrlOfficial: String,
    val imgUrlMini: String,
    val imgUrlPaint: String,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int
) {
    override fun toString(): String {
        return "PokemonDTO(\n" +
                "\tname='$name', \n" +
                "\torder=$order, \n" +
                "\theight=$height, \n" +
                "\tweight=$weight, \n" +
                "\tspecie='$specie', \n" +
                "\timgUrlOfficial='$imgUrlOfficial', \n" +
                "\timgUrlMini='$imgUrlMini', \n" +
                "\timgUrlPaint='$imgUrlPaint', \n" +
                "\thp=$hp, attack=$attack, defense=$defense, \n" +
                "\tspecialAttack=$specialAttack, specialDefense=$specialDefense, \n" +
                "\tspeed=$speed)"
    }
}