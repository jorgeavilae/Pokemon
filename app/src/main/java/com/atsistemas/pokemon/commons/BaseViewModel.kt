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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atsistemas.data.remote.ResultHandler
import com.atsistemas.pokemon.utils.SingleLiveEvent


@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _showMessage = SingleLiveEvent<String>()
    val showMessage: LiveData<String>
        get() = _showMessage

    private val _showError = SingleLiveEvent<String>()
    val showError: LiveData<String>
        get() = _showError

    protected fun setShowLoading(isLoading: Boolean) {
        _isLoading.postValue(isLoading)
    }

    protected fun setShowMessage(text: String) {
        _showMessage.postValue(text)
    }

    protected fun setShowError(errorMessage: String) {
        _showError.postValue(errorMessage)
    }

    protected fun setShowError(resultHandler: ResultHandler<*>) {
        when (resultHandler) {
            is ResultHandler.HttpError ->
                setShowError("HttpError ${resultHandler.code}")
            is ResultHandler.GenericError ->
                setShowError("GenericError: ${resultHandler.message}")
            is ResultHandler.NetworkError ->
                setShowError(Constants.NETWORK_ERROR)
            else ->
                setShowError(Constants.NETWORK_ERROR)
        }
    }
}