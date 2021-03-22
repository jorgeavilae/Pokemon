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

package com.atsistemas.pokemon.commons.uicomponents

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.atsistemas.pokemon.R
import com.atsistemas.pokemon.databinding.DialogErrorBinding

class ErrorDialog(
    context: Context,
    private val mTitle: String,
    private val mBody: String,
    private val mButton: String?,
    private val mListener: View.OnClickListener?
) : Dialog(context) {
    private lateinit var binding: DialogErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DialogErrorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.dialogErrorTitle.text = mTitle
        binding.dialogErrorBody.text = mBody

        if (mButton == null && mListener == null) {
            binding.dialogErrorButton.visibility = View.GONE
        } else {
            binding.dialogErrorButton.text = mButton
            binding.dialogErrorButton.setOnClickListener(mListener)
        }
    }

    override fun show() {
        super.show()

        window?.attributes?.windowAnimations = R.style.ErrorDialogAnimation
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}