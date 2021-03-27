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

package com.atsistemas.pokemon.main_activity.profile.ui

import android.os.Bundle
import android.view.*
import com.atsistemas.pokemon.R
import com.atsistemas.pokemon.commons.BaseFragment
import com.atsistemas.pokemon.commons.addAfterTextChangedListener
import com.atsistemas.pokemon.databinding.FragmentProfileBinding
import com.atsistemas.pokemon.main_activity.profile.vm.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment() {

    private val profileViewModel: ProfileViewModel by viewModel()

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Hacer estos componentes lifecycle-aware hace que sepan cuando
        // se destruyen y limpian variables (binding = null).
        binding.profileInputBadges.setLifecycleObservable(lifecycle)
        binding.profileInputPokedex.setLifecycleObservable(lifecycle)
        binding.profileInputTimeHours.setLifecycleObservable(lifecycle)
        binding.profileInputTimeMinutes.setLifecycleObservable(lifecycle)

        setListeners()

        return binding.root
    }

    // Guarda en persistencia el contenido del EditText cuando cambia.
    private fun setListeners() {
        // See commons/Extensions.kt
        binding.profileInputName.editText?.addAfterTextChangedListener {
            it?.let { inputText ->
                profileViewModel.setName(inputText.toString())
            }
        }

        binding.profileInputBadges.setOnValueUpdateListener {
            profileViewModel.setBadges(it)
        }

        binding.profileInputPokedex.setOnValueUpdateListener {
            profileViewModel.setPokedex(it)
        }

        binding.profileInputTimeHours.setOnValueUpdateListener {
            profileViewModel.setTime(it, binding.profileInputTimeMinutes.count)
        }
        binding.profileInputTimeMinutes.setOnValueUpdateListener {
            profileViewModel.setTime(binding.profileInputTimeHours.count, it)
        }
    }

    override fun loadObservers() {
        // Ongoing observers for TextViews in card
        profileViewModel.name.observe(viewLifecycleOwner) {
            binding.profileName.text = it?.toString()
        }
        profileViewModel.badges.observe(viewLifecycleOwner) {
            binding.profileBadges.text = it?.toString()
        }
        profileViewModel.pokedex.observe(viewLifecycleOwner) {
            binding.profilePokedex.text = it?.toString()
        }
        profileViewModel.time.observe(viewLifecycleOwner) {
            binding.profileTime.text = it?.toString()
        }

        // Top limit for pokedex input
        profileViewModel.pokedexTotal.observe(viewLifecycleOwner) {
            it?.let {
                binding.profileInputPokedex.maxCount = it
            }
        }

        // Single event observers for EditTexts
        profileViewModel.nameEvent.observe(viewLifecycleOwner) {
            binding.profileInputName.editText?.setText(it?.toString())
        }
        profileViewModel.badgesEvent.observe(viewLifecycleOwner) {
            it?.let {
                binding.profileInputBadges.count = it
            }
        }
        profileViewModel.pokedexEvent.observe(viewLifecycleOwner) {
            it?.let {
                binding.profileInputPokedex.count = it
            }
        }
        profileViewModel.hoursEvent.observe(viewLifecycleOwner) {
            it?.let {
                binding.profileInputTimeHours.count = it
            }
        }
        profileViewModel.minutesEvent.observe(viewLifecycleOwner) {
            it?.let {
                binding.profileInputTimeMinutes.count = it
            }
        }
    }

    override fun onStart() {
        super.onStart()
        profileViewModel.fetchData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_preferences_id -> {
                profileViewModel.deletePreferences()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}