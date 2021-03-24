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
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.atsistemas.pokemon.commons.*
import com.atsistemas.pokemon.databinding.FragmentProfileBinding
import com.atsistemas.pokemon.main_activity.profile.vm.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment() {

    private val profileViewModel: ProfileViewModel by viewModel()

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        setEditTextChangeListeners()

        // Show input for this TextView
        binding.profileName.setOnClickListener {
            binding.profileInputName.visibility = View.VISIBLE
            binding.profileInputName.editText?.setText(binding.profileName.text)
            binding.profileInputName.editText?.requestFocus()
            (activity as? BaseActivity)?.showKeyboard()
        }
        // Hide this input
        binding.profileInputName.editText?.setOnEditorActionListener { _, actionId, event ->
            val isDoneOrEnter = (actionId == EditorInfo.IME_ACTION_DONE ||
                    event.keyCode == KeyEvent.KEYCODE_ENTER ||
                    event.keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER)
            return@setOnEditorActionListener if (isDoneOrEnter) {
                binding.profileInputName.visibility = View.GONE
                binding.profileInputName.editText?.clearFocus()
                (activity as? BaseActivity)?.hideKeyboard()
                true
            } else {
                false
            }
        }

        return binding.root
    }

    private fun setEditTextChangeListeners() {
        // See commons/Extensions.kt
        binding.profileInputName.editText?.addAfterTextChangedListener {
            it?.let { inputText ->
                profileViewModel.setName(inputText.toString())
            }
        }
        binding.profileInputTime.editText?.addAfterTextChangedListener {
            it?.let { inputText ->
                profileViewModel.setName(inputText.toString())
            }
        }
        binding.profileInputBadges.editText?.addAfterTextChangedListener {
            it?.let { inputText ->
                profileViewModel.setName(inputText.toString())
            }
        }
        binding.profileInputPokedex.editText?.addAfterTextChangedListener {
            it?.let { inputText ->
                profileViewModel.setName(inputText.toString())
            }
        }
    }

    override fun loadObservers() {
        profileViewModel.name.observe(viewLifecycleOwner) {
            binding.profileName.text = it?.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}