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

package com.atsistemas.pokemon.main_activity.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.atsistemas.data.utils.PokemonDTOUtils
import com.atsistemas.pokemon.R
import com.atsistemas.pokemon.commons.BaseFragment
import com.atsistemas.pokemon.databinding.FragmentDetailBinding
import com.atsistemas.pokemon.utils.SharedPokemonViewModel
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DetailFragment : BaseFragment() {

    private val sharedViewModel: SharedPokemonViewModel by sharedViewModel()

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun loadObservers() {
        sharedViewModel.pokemon.observe(viewLifecycleOwner) {
            it?.let { pokemonDTO ->
                loadUrlPngIntoImageView(pokemonDTO.imgUrlMiniFront, binding.detailFront, R.drawable.pikachu_mini)
                loadUrlPngIntoImageView(pokemonDTO.imgUrlMiniBack, binding.detailBack, R.drawable.pikachu_mini)
                binding.detailId.text = "ID: ${pokemonDTO.id}"
                binding.detailName.text = "${pokemonDTO.name}"
                binding.detailOrder.text = "Order in whole Pokedex: ${pokemonDTO.order}"
                binding.detailWeight.text = "Weight: ${"%.2f".format(pokemonDTO.weight)} kg"
                binding.detailHeight.text = "Height: ${"%.2f".format(pokemonDTO.height)} m"
                binding.detailTypes.text =
                    "Tipos: ${PokemonDTOUtils.convertListTypesToString(pokemonDTO.types)}"
                loadUrlPngIntoImageView(pokemonDTO.imgUrlOfficial, binding.detailOfficial, R.drawable.pikachu_official)
                binding.detailStatHp.text = "HP: ${pokemonDTO.hp}"
                binding.detailStatAttack.text = "Attack: ${pokemonDTO.attack}"
                binding.detailStatDefense.text = "Defense: ${pokemonDTO.defense}"
                binding.detailStatSpAttack.text = "Special Attack: ${pokemonDTO.specialAttack}"
                binding.detailStatSpDefense.text = "Special Defense: ${pokemonDTO.specialDefense}"
                binding.detailStatSpeed.text = "Speed: ${pokemonDTO.speed}"
            }
        }
    }

    private fun loadUrlPngIntoImageView(
        url: String,
        imageView: ImageView,
        placeholderResId: Int? = null
    ) {
        val glide = Glide.with(this).load(url)
        placeholderResId?.let { glide.placeholder(placeholderResId) }
        glide.into(imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}