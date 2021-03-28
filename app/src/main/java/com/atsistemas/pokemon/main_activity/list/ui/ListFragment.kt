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

import android.os.Bundle
import android.view.*
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.atsistemas.data.models.PokemonDTO
import com.atsistemas.pokemon.R
import com.atsistemas.pokemon.commons.BaseFragment
import com.atsistemas.pokemon.commons.uicomponents.ErrorDialog
import com.atsistemas.pokemon.commons.uicomponents.SuccessDialog
import com.atsistemas.pokemon.databinding.FragmentListBinding
import com.atsistemas.pokemon.main_activity.list.vm.ListViewModel
import com.atsistemas.pokemon.utils.SharedPokemonViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : BaseFragment(), CellClickListener {

    private val listViewModel: ListViewModel by viewModel()
    private val sharedViewModel: SharedPokemonViewModel by sharedViewModel()

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding get() = _binding!!

    private var adapter: PokemonAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.swipeRefreshPokemonList.setColorSchemeResources(
            R.color.progress_color_1,
            R.color.progress_color_2,
            R.color.progress_color_3
        )
        binding.swipeRefreshPokemonList.setOnRefreshListener {
            listViewModel.fetchData()
        }

        binding.pokemonList.layoutManager = LinearLayoutManager(activity)
        adapter = PokemonAdapter(this)
        binding.pokemonList.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Aplaza la animación de la transición hasta que la lista se muestre
        // (ver observer de pokemons en loadObservers()).
        postponeEnterTransition()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun loadObservers() {
        listViewModel.pokemons.observe(viewLifecycleOwner) {
            adapter?.submitList(it)

            if (it.isNullOrEmpty()) {
                binding.listContainerEmpty.visibility = View.VISIBLE
            } else {
                binding.listContainerEmpty.visibility = View.INVISIBLE
            }

            // Ahora que la lista muestra los items, inicia la animación de la transición
            // (postpuesta en onViewCreated())
            (view?.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }

        listViewModel.showMessage.observe(viewLifecycleOwner) {
            successDialog = activity?.let { activity ->
                SuccessDialog(activity, it)
            }
            successDialog?.show()
        }

        listViewModel.showError.observe(viewLifecycleOwner) {
            errorDialog = activity?.let { activity ->
                ErrorDialog(activity, getString(R.string.alert), it, getString(R.string.close)) {
                    errorDialog?.dismiss()
                }
            }
            errorDialog?.setCancelable(false)
            errorDialog?.show()
        }

        listViewModel.isLoading.observe(viewLifecycleOwner) {
            it?.let { isLoading ->
                binding.swipeRefreshPokemonList.isRefreshing = isLoading
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_db_id -> {
                listViewModel.deleteDB()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

    override fun onCellClickListener(
        viewHolder: PokemonAdapter.ViewHolder,
        pokemonDTO: PokemonDTO
    ) {
        sharedViewModel.setPokemon(pokemonDTO)

        // Crea un bundle con los sharedElements que participan en la animación de la transición.
        val extras = FragmentNavigatorExtras(
            binding.listContainer to binding.listContainer.transitionName,
            viewHolder.binding.itemName to viewHolder.binding.itemName.transitionName,
            viewHolder.binding.itemFront to viewHolder.binding.itemFront.transitionName,
            viewHolder.binding.itemBack to viewHolder.binding.itemBack.transitionName
        )

        findNavController().navigate(
            R.id.action_navigation_list_to_detailFragment,
            null,
            null,
            extras
        )
    }
}