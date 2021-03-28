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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.atsistemas.data.models.PokemonDTO
import com.atsistemas.data.utils.PokemonDTOUtils
import com.atsistemas.pokemon.R
import com.atsistemas.pokemon.commons.Constants
import com.atsistemas.pokemon.databinding.ItemPokemonBinding
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette

class PokemonAdapter(private val cellClickListener: CellClickListener) :
    ListAdapter<PokemonDTO, PokemonAdapter.ViewHolder>(PokemonDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class ViewHolder(val binding: ItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root), BitmapPalette.CallBack {

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
            binding.itemType.text = PokemonDTOUtils.convertListTypesToString(pokemonDTO.types)
            binding.itemWeight.text = this.itemView.context.resources
                .getString(R.string.item_weight_format, pokemonDTO.weight)

            this.itemView.setOnClickListener {
                cellClickListener.onCellClickListener(this, pokemonDTO)
            }

            // Establece los identificadores de los sharedElements que participan en la animación
            // de la transición en base al identificador del pokemon que es único en la lista.
            binding.itemName.transitionName =
                Constants.SHARED_ELEMENT_TRANSITION_NAME + pokemonDTO.id
            binding.itemFront.transitionName =
                Constants.SHARED_ELEMENT_TRANSITION_FRONT + pokemonDTO.id
            binding.itemBack.transitionName =
                Constants.SHARED_ELEMENT_TRANSITION_BACK + pokemonDTO.id
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
                    binding.itemType.setTextColor(colorSubtitle)
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

class PokemonDiffUtil : DiffUtil.ItemCallback<PokemonDTO>() {
    override fun areItemsTheSame(oldItem: PokemonDTO, newItem: PokemonDTO): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PokemonDTO, newItem: PokemonDTO): Boolean {
        return oldItem == newItem
    }

}

interface CellClickListener {
    fun onCellClickListener(viewHolder: PokemonAdapter.ViewHolder, pokemonDTO: PokemonDTO)
}