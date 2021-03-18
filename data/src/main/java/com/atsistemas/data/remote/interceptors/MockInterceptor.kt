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

    override fun intercept(chain: Interceptor.Chain): Response {
        var res: Response? = null

        // Pokemon de la generación 1
        if (res == null)
            res = getMockResponse("generation/1", R.raw.generation_1, chain, responseCodeOK)

        // Un Pokemon
        if (res == null)
            res = getMockResponse("pokemon/bulbasaur", R.raw.pokemon_bulbasaur, chain, responseCodeOK)

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