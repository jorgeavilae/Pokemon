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

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import com.atsistemas.data.models.PokemonDTO
import com.atsistemas.data.utils.PokemonDTOUtils
import com.atsistemas.pokemon.R
import com.atsistemas.pokemon.commons.BaseFragment
import com.atsistemas.pokemon.commons.Constants
import com.atsistemas.pokemon.databinding.FragmentDetailBinding
import com.atsistemas.pokemon.main_activity.MainActivity
import com.atsistemas.pokemon.utils.SharedPokemonViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
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
                showDataInUI(pokemonDTO)
            }
        }
    }

    private fun showDataInUI(pokemonDTO: PokemonDTO) {
        // Title and Name
        (activity as? MainActivity)?.supportActionBar?.title = pokemonDTO.name
        binding.detailName.text = pokemonDTO.name

        // Pokemon Image
        loadUrlIntoImageView(pokemonDTO.imgUrlOfficial, binding.detailOfficial, true) {
            it?.let { palette ->
                /* Palette contiene los colores principales de una imagen */
                val colorBackground = palette.getDominantColor(Color.WHITE)
                binding.detailOfficial.background?.setTint(colorBackground)
            }
        }

        // Pokemon mini images
        loadUrlIntoImageView(pokemonDTO.imgUrlMiniFront, binding.detailFront)
        loadUrlIntoImageView(pokemonDTO.imgUrlMiniBack, binding.detailBack)

        // ID and Order
        binding.detailId.text = resources.getString(R.string.id_with_number, pokemonDTO.id)
        binding.detailOrder.text = resources.getString(R.string.order_with_number, pokemonDTO.order)

        // Height and Weight
        binding.detailHeight.text =
            resources.getString(R.string.height_with_number, pokemonDTO.height)
        binding.detailWeight.text =
            resources.getString(R.string.weight_with_number, pokemonDTO.weight)

        // Types
        binding.detailTypes.text = PokemonDTOUtils.convertListTypesToString(pokemonDTO.types, "\n")

        // Stats
        binding.detailStatHp.text = "${pokemonDTO.hp}"
        binding.detailStatAttack.text = "${pokemonDTO.attack}"
        binding.detailStatDefense.text = "${pokemonDTO.defense}"
        binding.detailStatSpAttack.text = "${pokemonDTO.specialAttack}"
        binding.detailStatSpDefense.text = "${pokemonDTO.specialDefense}"
        binding.detailStatSpeed.text = "${pokemonDTO.speed}"

        // Establece los identificadores de los sharedElements que participan en la animación
        // de la transición en base al identificador del pokemon mostrado.
        binding.detailName.transitionName =
            Constants.SHARED_ELEMENT_TRANSITION_NAME + pokemonDTO.id
        binding.detailFront.transitionName =
            Constants.SHARED_ELEMENT_TRANSITION_FRONT + pokemonDTO.id
        binding.detailBack.transitionName =
            Constants.SHARED_ELEMENT_TRANSITION_BACK + pokemonDTO.id

        // Ahora que los datos se muestran, inicia la animación de la transición
        // (postpuesta en onViewCreated())
        (view?.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }

    }

    // Método utilizado para cargar imágenes de internet en un ImageView. Usa Glide para descargar
    //  la imagen, y GlidePalette para extraer un Palette con el que colorear elementos. Cambia
    //  la visibility de un ProgressBar cuando carga la imagen.
    private fun loadUrlIntoImageView(
        url: String,
        imageView: ImageView,
        hideProgress: Boolean = false,
        paletteCallback: BitmapPalette.CallBack? = null
    ) {
        val glide = Glide.with(this).load(url)
            .transition(DrawableTransitionOptions.withCrossFade().crossFade())

        paletteCallback?.let {
            glide.addListener(GlidePalette.with(url).intoCallBack(it))
        }

        if (hideProgress)
            glide.addListener(object: RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.detailOfficialProgress.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.detailOfficialProgress.visibility = View.GONE
                    return false
                }
            })

        glide.into(imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}