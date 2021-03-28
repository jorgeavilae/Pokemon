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

package com.atsistemas.data.remote.interceptors

import android.app.Application
import com.atsistemas.data.R
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.BufferedReader
import java.io.InputStreamReader

@Suppress("SameParameterValue", "unused")
class MockInterceptor(private val application: Application) : Interceptor {

    private val responseCodeError = 408
    private val responseSessionError = 401
    private val responseCodeOK = 200

    @Suppress("SpellCheckingInspection")
    override fun intercept(chain: Interceptor.Chain): Response {
        var res: Response? = null

        // Generación 1 de pokemon
        if (res == null)
            res = getMockResponse("generation/1", R.raw.generation_1, chain, responseCodeOK)

        // Pokemon bulbasaur
        if (res == null)
            res = getMockResponse("pokemon/bulbasaur", R.raw.pokemon_001, chain, responseCodeOK)
        if (res == null)
            res = getMockResponse("pokemon/1/", R.raw.pokemon_001, chain, responseCodeOK)
        // Pokemon ivysaur
        if (res == null)
            res = getMockResponse("pokemon/ivysaur", R.raw.pokemon_002, chain, responseCodeOK)
        if (res == null)
            res = getMockResponse("pokemon/2/", R.raw.pokemon_002, chain, responseCodeOK)
        // Pokemon venusaur
        if (res == null)
            res = getMockResponse("pokemon/venusaur", R.raw.pokemon_003, chain, responseCodeOK)
        if (res == null)
            res = getMockResponse("pokemon/3/", R.raw.pokemon_003, chain, responseCodeOK)
        // Pokemon charmander
        if (res == null)
            res = getMockResponse("pokemon/charmander", R.raw.pokemon_004, chain, responseCodeOK)
        if (res == null)
            res = getMockResponse("pokemon/4/", R.raw.pokemon_004, chain, responseCodeOK)
        // Pokemon charmeleon
        if (res == null)
            res = getMockResponse("pokemon/charmeleon", R.raw.pokemon_005, chain, responseCodeOK)
        if (res == null)
            res = getMockResponse("pokemon/5/", R.raw.pokemon_005, chain, responseCodeOK)
        // Pokemon charizard
        if (res == null)
            res = getMockResponse("pokemon/charizard", R.raw.pokemon_006, chain, responseCodeOK)
        if (res == null)
            res = getMockResponse("pokemon/6/", R.raw.pokemon_006, chain, responseCodeOK)
        // Pokemon squirtle
        if (res == null)
            res = getMockResponse("pokemon/squirtle", R.raw.pokemon_007, chain, responseCodeOK)
        if (res == null)
            res = getMockResponse("pokemon/7/", R.raw.pokemon_007, chain, responseCodeOK)
        // Pokemon wartortle
        if (res == null)
            res = getMockResponse("pokemon/wartortle", R.raw.pokemon_008, chain, responseCodeOK)
        if (res == null)
            res = getMockResponse("pokemon/8/", R.raw.pokemon_008, chain, responseCodeOK)
        // Pokemon blastoise
        if (res == null)
            res = getMockResponse("pokemon/blastoise", R.raw.pokemon_009, chain, responseCodeOK)
        if (res == null)
            res = getMockResponse("pokemon/9/", R.raw.pokemon_009, chain, responseCodeOK)

        // Pokemon por defecto: pikachu
        // Esta dirección puede coincidir con varias url.
        // Sería mejor utilizar RegEx en MockInterceptor.getMockResponse()
        if (res == null)
            res = getMockResponse("pokemon/", R.raw.pokemon_010, chain, responseCodeOK)


        return res ?: chain.proceed(chain.request())
    }

    private fun getMockResponse(
        urlContains: String,
        jsonResource: Int,
        chain: Interceptor.Chain,
        responseCode: Int
    ): Response? {
        return getMockResponse(null, urlContains, jsonResource, chain, responseCode)
    }

    private fun getMockResponse(
        method: String?,
        urlContains: String,
        jsonResource: Int,
        chain: Interceptor.Chain,
        responseCode: Int
    ): Response? {
        if (method != null && !method.equals(chain.request().method, true)) {
            return null
        }
        val strUrl = getEndPoint(chain.request().url.toString())
        var contains = true
        val urls = urlContains.split("%")
        for (x in urls) {
            contains = contains && strUrl.contains(x)
        }
        if (contains) {
            return returnMockResponse(chain, responseCode, jsonResource, application)
        }
        return null
    }

    private fun getEndPoint(url: String): String {
        var endIndex = url.length
        if (url.contains("?")) {
            endIndex = url.lastIndexOf("?")
        }
        return url.subSequence(0, endIndex).toString()
    }

    private fun returnMockResponse(
        chain: Interceptor.Chain,
        responseCode: Int,
        responseString: String
    ): Response {
        return Response.Builder()
            .protocol(Protocol.HTTP_1_0)
            .code(responseCode)
            .request(chain.request())
            .message(responseString)
            .body(
                responseString.toByteArray()
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
            .addHeader("content-type", "application/json")
            .build()
    }

    private fun returnMockResponse(
        chain: Interceptor.Chain,
        responseCode: Int,
        resource: Int,
        app: Application
    ): Response {
        val responseString = getStringFromResource(resource, app)
        return returnMockResponse(chain, responseCode, responseString)
    }

    private fun getStringFromResource(resource: Int, app: Application): String {
        val inputStream = app.resources.openRawResource(resource)
        val br = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        var line: String?
        line = br.readLine()
        while (line != null) {
            sb.append(line)
            line = br.readLine()
        }
        br.close()
        return sb.toString()
    }
}