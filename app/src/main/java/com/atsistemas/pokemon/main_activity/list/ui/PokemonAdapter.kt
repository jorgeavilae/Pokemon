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

package com.atsistemas.pokemon.main_activity.list.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.atsistemas.data.models.PokemonDTO
import com.atsistemas.pokemon.databinding.ItemPokemonBinding

class PokemonAdapter(private val cellClickListener: CellClickListener) :
    RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private lateinit var binding: ItemPokemonBinding
    private var mValues: List<PokemonDTO>? = null

    fun submitList(values: List<PokemonDTO>) = values.also {
        mValues = it
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mValues?.let {
            holder.bind(it[position])
        } ?: clearList()
    }

    private fun clearList() {
        mValues = emptyList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mValues?.size ?: 0

    inner class ViewHolder(binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemonDTO: PokemonDTO) {
//            binding.itemImage.setImageDrawable(R.drawable.pikachu_official)
            binding.itemName.text = pokemonDTO.name
            binding.itemSpecie.text = pokemonDTO.specie
            binding.itemWeight.text = pokemonDTO.weight.toString()

            binding.root.setOnClickListener {
                cellClickListener.onCellClickListener(pokemonDTO)
            }
        }
    }
}

interface CellClickListener {
    fun onCellClickListener(pokemonDTO: PokemonDTO)
}