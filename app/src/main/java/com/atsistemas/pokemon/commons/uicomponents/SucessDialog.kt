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
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.atsistemas.pokemon.R
import com.atsistemas.pokemon.commons.Constants
import com.atsistemas.pokemon.databinding.DialogSuccessBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SuccessDialog(private val mContext: Context, private val mBody: String) : Dialog(mContext) {
    private lateinit var binding: DialogSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DialogSuccessBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.dialogOkBody.text = mBody
    }

    override fun show() {
        super.show()

        window?.attributes?.windowAnimations = R.style.SuccessDialogAnimation
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        (mContext as FragmentActivity).lifecycleScope.launch {
            delay(Constants.DIALOG_SUCCESS_SHOW)
            this@SuccessDialog.dismiss()
        }
    }
}