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

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.atsistemas.pokemon.R
import com.atsistemas.pokemon.databinding.LayoutDpadCountBinding

// Custom View Layout que representa un contador con unos botones que lo aumentan o lo disminuyen.
// Los botones pueden orientarse en vertical o en horizontal.
// Utilizado en la pantalla Profile para introducir datos numéricos.
class DPadCountLayout(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    // Interfaz para avisar de cuando cambia el valor del contador
    interface ValueUpdateListener {
        fun onValueUpdate(value: Int)
    }

    // Valores por defecto de los atributos declarados en attrs.xml/DPadCountLayout
    private val defaultColor = ContextCompat.getColor(context, android.R.color.darker_gray)
    private val defaultLabelAppearance = R.style.TextAppearance_AppCompat_Caption
    private val defaultCountAppearance = R.style.TextAppearance_AppCompat_Body1
    private val defaultMinCount = 0
    private val defaultMaxCount = 1

    // Color de los botones
    var buttonColor: Int = defaultColor
    // Representa al botón de incremento del contador en esta clase (horizontal/vertical)
    private lateinit var incrementButton: ImageButton
    // Representa al botón de decremento del contador en esta clase (horizontal/vertical)
    private lateinit var decrementButton: ImageButton

    // Listener que se activa cuando cambia el contador
    var valueUpdateListener: ValueUpdateListener? = null

    // Límites del valor del contador. Con el setter se controla que los botones
    // se activen/desactiven si los límites cambian.
    var minCount: Int = defaultMinCount
        set(value) {
            field = value
            enableButtons(field)
        }
    var maxCount: Int = defaultMaxCount
        set(value) {
            field = value
            enableButtons(field)
        }

    // Contador.
    var count: Int = minCount
        // Con el setter se controla cada vez que el valor cambia:
        set(value) {
            field = value
            //  - se actualiza el TextView
            binding.dpadCountText.text = field.toString()
            //  - se habilitan/deshabilitan los botones
            enableButtons(field)
            //  - se ejecuta el listener
            valueUpdateListener?.onValueUpdate(field)
            Log.d("ASD", field.toString())
        }

    // Views de este componente
    private val binding: LayoutDpadCountBinding
//    todo para ver cuando cambia set(value) {
//        field = value
//        Log.d("ASD","me voy a "+value)
//    }

    // Constructor
    init {
        val inflater = LayoutInflater.from(context)
        binding = LayoutDpadCountBinding.inflate(inflater, this, true)

        // Se comprueban los atributos establecidos en el XML
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.DPadCountLayout)
            typedArray.apply {

                // DPadCountLayout:orientation
                setOrientation(getInteger(R.styleable.DPadCountLayout_orientation, 0))

                // DPadCountLayout:buttonColor
                setButtonsColor(getColor(R.styleable.DPadCountLayout_buttonColor, defaultColor))

                // DPadCountLayout:label
                setLabel(getString(R.styleable.DPadCountLayout_label) ?: "")

                // DPadCountLayout:labelTextAppearance
                setLabelTextAppearance(
                    getResourceId(
                        R.styleable.DPadCountLayout_labelTextAppearance,
                        defaultLabelAppearance
                    )
                )

                // DPadCountLayout:countTextAppearance
                setCountTextAppearance(
                    getResourceId(
                        R.styleable.DPadCountLayout_countTextAppearance,
                        defaultCountAppearance
                    )
                )

                // DPadCountLayout:minCount y DPadCountLayout:maxCount
                minCount = getInteger(R.styleable.DPadCountLayout_minCount, defaultMinCount)
                maxCount = getInteger(R.styleable.DPadCountLayout_maxCount, defaultMaxCount)

                // El typedArray debe reciclarse
                recycle()
            }
        }

        this.count = minCount
        setListeners()
    }

    private fun setOrientation(orientation: Int) {
        when (orientation) {
            0 -> { // Horizontal
                // Show horizontal buttons
                binding.dpadLeftButton.visibility = VISIBLE
                binding.dpadRightButton.visibility = VISIBLE
                // Hide vertical buttons
                binding.dpadTopButton.visibility = GONE
                binding.dpadBottomButton.visibility = GONE
                // Set buttons
                incrementButton = binding.dpadRightButton
                decrementButton = binding.dpadLeftButton
            }
            else -> { // Vertical
                // Hide horizontal buttons
                binding.dpadLeftButton.visibility = GONE
                binding.dpadRightButton.visibility = GONE
                // Show vertical buttons
                binding.dpadTopButton.visibility = VISIBLE
                binding.dpadBottomButton.visibility = VISIBLE
                // Set buttons
                incrementButton = binding.dpadTopButton
                decrementButton = binding.dpadBottomButton
            }
        }
    }

    private fun setButtonsColor(color: Int) {
        buttonColor = color
        decrementButton.background.setTint(buttonColor)
        incrementButton.background.setTint(buttonColor)
    }

    private fun setLabel(label: String) {
        binding.dpadLabel.text = label
    }

    private fun setLabelTextAppearance(resourceId: Int) {
        TextViewCompat.setTextAppearance(binding.dpadLabel, resourceId)
    }

    private fun setCountTextAppearance(resourceId: Int) {
        TextViewCompat.setTextAppearance(binding.dpadCountText, resourceId)
    }

    private fun setListeners() {
        decrementButton.setOnClickListener {
            if (count > minCount) count--
        }
        incrementButton.setOnClickListener {
            if (count + 1 <= maxCount) count++
        }
    }

    fun setOnValueUpdateListener(action: (Int) -> Unit) {
        setOnValueUpdateListener(object : ValueUpdateListener {
            override fun onValueUpdate(value: Int) {
                action(value)
            }
        })
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun setOnValueUpdateListener(listener: ValueUpdateListener) {
        this.valueUpdateListener = listener
    }

    private fun enableButtons(count: Int) {
        when {
            count == minCount && count == maxCount -> {
                decrementButton.isEnabled = false
                decrementButton.background.setTint(defaultColor)
                incrementButton.isEnabled = false
                incrementButton.background.setTint(defaultColor)
            }
            count <= minCount -> {
                decrementButton.isEnabled = false
                decrementButton.background.setTint(defaultColor)
                incrementButton.isEnabled = true
                incrementButton.background.setTint(buttonColor)
            }
            count >= maxCount -> {
                decrementButton.isEnabled = true
                decrementButton.background.setTint(buttonColor)
                incrementButton.isEnabled = false
                incrementButton.background.setTint(defaultColor)
            }
            else -> {
                decrementButton.isEnabled = true
                decrementButton.background.setTint(buttonColor)
                incrementButton.isEnabled = true
                incrementButton.background.setTint(buttonColor)
            }
        }
    }
}
