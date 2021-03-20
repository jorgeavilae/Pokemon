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

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.atsistemas.data.models.PokemonDTO
import com.atsistemas.pokemon.R
import com.atsistemas.pokemon.databinding.ItemPokemonBinding
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette

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

    inner class ViewHolder(private val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root), BitmapPalette.CallBack {

        fun bind(pokemonDTO: PokemonDTO) {
            Glide.with(this.itemView)
                .load(pokemonDTO.imgUrlMiniBack)
                .placeholder(R.drawable.pikachu_mini)
                .into(binding.itemBack)

            Glide.with(this.itemView)
                .load(pokemonDTO.imgUrlMiniFront)
                .placeholder(R.drawable.pikachu_mini)
                /* GlidePalette extrae el Palette de una imagen dada su URL. */
                .listener(GlidePalette.with(pokemonDTO.imgUrlMiniFront).intoCallBack(this))
                .into(binding.itemFront)

            binding.itemName.text = pokemonDTO.name
            binding.itemSpecie.text = pokemonDTO.specie
            binding.itemWeight.text = pokemonDTO.weight.toString()

            this.itemView.setOnClickListener {
                cellClickListener.onCellClickListener(pokemonDTO)
            }
        }

        override fun onPaletteLoaded(p: Palette?) {
            p?.let { palette ->
                /* Palette contiene los colores principales de una imagen */
                val colorBackground = palette.getDominantColor(Color.WHITE)
                binding.itemBackground.drawable.setTint(colorBackground)

                /* Swatch tiene un color para títulos con el contraste suficiente */
                palette.dominantSwatch?.titleTextColor?.let { colorTitle ->
                    binding.itemName.setTextColor(colorTitle)
                }

                /* Swatch tiene un color para cuerpo de texto con el contraste suficiente */
                palette.dominantSwatch?.bodyTextColor?.let { colorSubtitle ->
                    binding.itemSpecie.setTextColor(colorSubtitle)
                    binding.itemWeight.setTextColor(colorSubtitle)

                    /* Drawable comparte su estado entre todos los Drawables.
                     * Cambiar el color de un Drawable los cambia todos.
                     * Se debe clonar el Drawable (mutate()), cambiarle el estado
                     * y reasignarlo al ImageView. */
                    var draw = binding.itemIcLeaf.drawable.mutate()
                    draw.setTint(colorSubtitle)
                    binding.itemIcLeaf.setImageDrawable(draw)

                    draw = binding.itemIcWeight.drawable.mutate()
                    draw.setTint(colorSubtitle)
                    binding.itemIcWeight.setImageDrawable(draw)
                }
            }
        }
    }
}

interface CellClickListener {
    fun onCellClickListener(pokemonDTO: PokemonDTO)
}