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
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import com.atsistemas.data.utils.PokemonDTOUtils
import com.atsistemas.pokemon.R
import com.atsistemas.pokemon.commons.BaseFragment
import com.atsistemas.pokemon.databinding.FragmentDetailBinding
import com.atsistemas.pokemon.main_activity.MainActivity
import com.atsistemas.pokemon.utils.SharedPokemonViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DetailFragment : BaseFragment() {

    private val sharedViewModel: SharedPokemonViewModel by sharedViewModel()

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establece las animaciones de entrada y salida de este Fragment, declaradas en XML
        this.sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_pokemon_details_enter)
        this.sharedElementReturnTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_pokemon_details_return)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Aplaza la animación de la transición hasta que los datos se muestren.
        // (ver final del observer del pokemon en loadObservers()).
        postponeEnterTransition()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun loadObservers() {
        sharedViewModel.pokemon.observe(viewLifecycleOwner) {
            it?.let { pokemonDTO ->
                // todo adecentar
                (activity as? MainActivity)?.supportActionBar?.title = "${pokemonDTO.name}"
                loadUrlIntoImageView(
                    pokemonDTO.imgUrlMiniFront,
                    binding.detailFront,
                    R.drawable.pikachu_mini
                )
                loadUrlIntoImageView(
                    pokemonDTO.imgUrlMiniBack,
                    binding.detailBack,
                    R.drawable.pikachu_mini
                )
                binding.detailId.text = "ID: ${pokemonDTO.id}"
                binding.detailName.text = "${pokemonDTO.name}"
                binding.detailOrder.text = "Pokemon No. ${pokemonDTO.order}"
                binding.detailWeight.text = "Weight: ${"%.2f".format(pokemonDTO.weight)} kg"
                binding.detailHeight.text = "Height: ${"%.2f".format(pokemonDTO.height)} m"
                binding.detailTypes.text =
                    "${PokemonDTOUtils.convertListTypesToString(pokemonDTO.types, "\n")}"
                loadUrlIntoImageView(pokemonDTO.imgUrlOfficial, binding.detailOfficial)
                binding.detailStatHp.text = "${pokemonDTO.hp}"
                binding.detailStatAttack.text = "${pokemonDTO.attack}"
                binding.detailStatDefense.text = "${pokemonDTO.defense}"
                binding.detailStatSpAttack.text = "${pokemonDTO.specialAttack}"
                binding.detailStatSpDefense.text = "${pokemonDTO.specialDefense}"
                binding.detailStatSpeed.text = "${pokemonDTO.speed}"
                val color = ContextCompat.getColor(requireContext(), R.color.pokemon_blue)
                binding.detailOfficial.background?.setTint(color)

                // Establece los identificadores de los sharedElements que participan en la animación
                // de la transición en base al identificador del pokemon mostrado.
                binding.detailName.transitionName = "transition_pokemon_name_" + pokemonDTO.id
                binding.detailFront.transitionName = "transition_pokemon_front_" + pokemonDTO.id
                binding.detailBack.transitionName = "transition_pokemon_back_" + pokemonDTO.id

                // Ahora que los datos se muestran, inicia la animación de la transición
                // (postpuesta en onViewCreated())
                (view?.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }
            }
        }
    }

    private fun loadUrlIntoImageView(
        url: String, imageView: ImageView, placeholderResId: Int? = null
    ) {
        val glide = Glide.with(this).load(url)
            .transition(DrawableTransitionOptions.withCrossFade().crossFade())
        placeholderResId?.let {
            glide.placeholder(placeholderResId)
        }
        glide.into(imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}