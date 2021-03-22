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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class ListFragment : BaseFragment() {

    private val listViewModel: ListViewModel by viewModel()
    private val sharedViewModel: SharedPokemonViewModel by sharedViewModel()

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding get() = _binding!!

    private var adapter: PokemonAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.swipeRefreshPokemonList.setOnRefreshListener {
            listViewModel.fetchData()
        }

        binding.pokemonList.layoutManager = LinearLayoutManager(activity)
        adapter = PokemonAdapter(object : CellClickListener {
            override fun onCellClickListener(pokemonDTO: PokemonDTO) {
                sharedViewModel.setPokemon(pokemonDTO)
                findNavController().navigate(R.id.action_navigation_list_to_detailFragment)
            }
        })
        binding.pokemonList.adapter = adapter

        return binding.root
    }

    override fun loadObservers() {
        listViewModel.pokemons.observe(viewLifecycleOwner) {
            adapter?.submitList(listOf(it).flatten())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }
}