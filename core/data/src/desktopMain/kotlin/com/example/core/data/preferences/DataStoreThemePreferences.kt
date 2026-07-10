package com.example.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.core.domain.preferences.ThemePreference
import com.example.core.domain.preferences.ThemePreferences
import kotlinx.coroutines.flow.Flow

class DataStoreThemePreferences(
    private val dataStore: DataStore<Preferences>
) : ThemePreferences {

    override fun observeThemePreference(): Flow<ThemePreference> {
        TODO("Not yet implemented")
    }

    override suspend fun updateThemePreference(theme: ThemePreference) {
        TODO("Not yet implemented")
    }

}