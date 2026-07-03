package com.example.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.core.data.dto.AuthInfoSerializable
import com.example.core.data.mappers.toDomain
import com.example.core.data.mappers.toSerializable
import com.example.core.domain.auth.AuthInfo
import com.example.core.domain.auth.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class DataStoreStorageSession(
    val dataStore: DataStore<Preferences>
) : SessionStorage {

    private val authInfoKey = stringPreferencesKey("KEY_AUTH_INFO")
    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun observeAuthInfo(): Flow<AuthInfo?> = with(dataStore) {
        return@with data.map { preferences ->
            val authInfoJson = preferences[authInfoKey]
            authInfoJson?.let {
                json.decodeFromString<AuthInfoSerializable>(it).toDomain()
            }
        }
    }

    override suspend fun set(info: AuthInfo?) = with(dataStore) {
        if (info == null) {
            edit { preferences ->
                preferences.remove(authInfoKey)
            }
            return@with
        }

        val authInfoJson = this@DataStoreStorageSession.json.encodeToString(info.toSerializable())
        edit { preferences ->
            preferences[authInfoKey] = authInfoJson
        }
    }

}