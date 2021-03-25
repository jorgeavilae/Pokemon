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

package com.atsistemas.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.atsistemas.data.commons.Constants
import com.atsistemas.data.commons.catchPreferencesException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map


class ProfilePreferencesWrapper(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.DATASTORE_NAME)

    // Name
    val preferencesName: LiveData<String> = context.dataStore.data
        .catch {
            catchPreferencesException(it)
        }.map { preferences ->
            val nameKey = stringPreferencesKey(Constants.PREFERENCES_NAME_KEY)
            preferences[nameKey] ?: ""
        }.asLiveData()

    suspend fun setName(name: String) {
        context.dataStore.edit { preferences ->
            val nameKey = stringPreferencesKey(Constants.PREFERENCES_NAME_KEY)
            preferences[nameKey] = name
        }
    }

    // Time
    val preferencesTime: LiveData<String> = context.dataStore.data
        .catch {
            catchPreferencesException(it)
        }.map { preferences ->
            val timeKey = stringPreferencesKey(Constants.PREFERENCES_TIME_KEY)
            preferences[timeKey] ?: ""
        }.asLiveData()

    suspend fun setTime(time: String) {
        context.dataStore.edit { preferences ->
            val timeKey = stringPreferencesKey(Constants.PREFERENCES_TIME_KEY)
            preferences[timeKey] = time
        }
    }

    // Badges
    val preferencesBadges: LiveData<String> = context.dataStore.data
        .catch {
            catchPreferencesException(it)
        }.map { preferences ->
            val badgesKey = stringPreferencesKey(Constants.PREFERENCES_BADGES_KEY)
            preferences[badgesKey] ?: ""
        }.asLiveData()

    suspend fun setBadges(badges: String) {
        context.dataStore.edit { preferences ->
            val badgesKey = stringPreferencesKey(Constants.PREFERENCES_BADGES_KEY)
            preferences[badgesKey] = badges
        }
    }

    // Pokedex
    val preferencesPokedex: LiveData<String> = context.dataStore.data
        .catch {
            catchPreferencesException(it)
        }.map { preferences ->
            val pokedexKey = stringPreferencesKey(Constants.PREFERENCES_POKEDEX_KEY)
            preferences[pokedexKey] ?: ""
        }.asLiveData()

    suspend fun setPokedex(pokedex: String) {
        context.dataStore.edit { preferences ->
            val pokedexKey = stringPreferencesKey(Constants.PREFERENCES_POKEDEX_KEY)
            preferences[pokedexKey] = pokedex
        }
    }
}
