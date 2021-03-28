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

package com.atsistemas.pokemon.commons

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView

// Reified es necesario para acceder a T::class.java. Inline es necesario para poner reified.
inline fun <reified T: BaseActivity> BaseActivity.startActivity() =
    this.startActivity(Intent(this, T::class.java))

fun EditText.addAfterTextChangedListener(action: (Editable?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //Not implemented
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //Not implemented
        }

        override fun afterTextChanged(s: Editable?) {
            action(s)
        }
    })
}

fun BottomNavigationView.showNav() {
    this.layoutParams?.let {
        // Get CoordinatorLayout.LayoutParams
        val coordinatorParams : CoordinatorLayout.LayoutParams? =
            (this.layoutParams as? CoordinatorLayout.LayoutParams)

        coordinatorParams?.let {
            // Get HideBottomViewOnScrollBehavior
            val hideScrollBehavior: HideBottomViewOnScrollBehavior<View>? =
                (it.behavior as? HideBottomViewOnScrollBehavior)

            // Use HideBottomViewOnScrollBehavior to show BottomNavigationView
            hideScrollBehavior?.slideUp(this)
        }
    }
}

fun BottomNavigationView.hideNav() {
    this.layoutParams?.let {
        // Get CoordinatorLayout.LayoutParams
        val coordinatorParams : CoordinatorLayout.LayoutParams? =
            (this.layoutParams as? CoordinatorLayout.LayoutParams)

        coordinatorParams?.let {
            // Get HideBottomViewOnScrollBehavior
            val hideScrollBehavior: HideBottomViewOnScrollBehavior<View>? =
                (it.behavior as? HideBottomViewOnScrollBehavior)

            // Use HideBottomViewOnScrollBehavior to show BottomNavigationView
            hideScrollBehavior?.slideDown(this)
        }
    }
}
