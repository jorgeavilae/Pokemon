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

package com.atsistemas.pokemon.splash_activity

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import com.atsistemas.pokemon.R
import com.atsistemas.pokemon.commons.BaseActivity
import com.atsistemas.pokemon.commons.Constants
import com.atsistemas.pokemon.commons.startActivity
import com.atsistemas.pokemon.main_activity.MainActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Elige un Gif de carga al azar
        val gifRandom = arrayOf(
            R.drawable.gif_character_evolution,
            R.drawable.gif_red_walking,
            R.drawable.gif_pikachu_run
        ).random()

        // Coloca el Gif en la ImageView
        Glide.with(this)
            .load(gifRandom)
            .into(findViewById(R.id.splash_image))

        // Extrae la Palette del Gif y colorea el fondo
        val bitmap = BitmapFactory.decodeResource(resources, gifRandom)
        Palette.from(bitmap).generate { palette ->
            findViewById<ConstraintLayout>(R.id.splash_container).background?.setTint(
                palette?.getDominantColor(Color.WHITE) ?: Color.WHITE
            )
            findViewById<ImageView>(R.id.splash_image).background?.setTint(
                palette?.getDominantColor(Color.WHITE) ?: Color.WHITE
            )
        }

        // Wait a few seconds
        lifecycleScope.launch(Dispatchers.Default) {
            delay(Constants.SPLASH_LOADING)
            startActivity<MainActivity>()
            finish()
        }
    }
}