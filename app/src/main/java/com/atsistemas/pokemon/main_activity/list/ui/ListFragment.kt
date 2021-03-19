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
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.atsistemas.data.models.PokemonDTO
import com.atsistemas.pokemon.commons.BaseFragment
import com.atsistemas.pokemon.databinding.FragmentListBinding
import com.atsistemas.pokemon.main_activity.list.vm.ListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : BaseFragment() {

    private val listViewModel: ListViewModel by viewModel()

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding get() = _binding!!

    private var adapter: PokemonAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.buttonList.setOnClickListener { listViewModel.fetchData() }

        binding.pokemonList.layoutManager = GridLayoutManager(activity, 2)
        adapter = PokemonAdapter(object : CellClickListener {
            override fun onCellClickListener(pokemonDTO: PokemonDTO) {
                Toast.makeText(activity,"Pokemon No. ${pokemonDTO.order}", Toast.LENGTH_SHORT).show()
            }
        })
        binding.pokemonList.adapter = adapter

        return binding.root
    }

    override fun loadObservers() {
        listViewModel.pokemons.observe(viewLifecycleOwner) {
            adapter?.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

}