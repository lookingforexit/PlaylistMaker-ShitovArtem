package com.example.playlistmaker.data.history

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.playlistmaker.domain.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class SearchHistoryRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): SearchHistoryRepository {
    companion object {
        private const val MAX_HISTORY_SIZE = 10
        private val HISTORY_KEY = stringPreferencesKey("search_history_json")
        private val gson = Gson()
        private val listType = object : TypeToken<List<String>>() {}.type
    }

    override suspend fun addToHistory(str: String) {
        if (str.isBlank()) return

        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                val currentJson = preferences[HISTORY_KEY]
                val currentList = if (currentJson != null) {
                    gson.fromJson<List<String>>(currentJson, listType)
                } else emptyList()
                val newList = mutableListOf(str).apply {
                    addAll(currentList.filter { it != str })
                    if (size > MAX_HISTORY_SIZE) removeAt(lastIndex)
                }
                preferences[HISTORY_KEY] = gson.toJson(newList)
            }
        }
    }

    override fun getHistory(): Flow<List<String>> {
        return dataStore.data.map { preferences ->
            val json = preferences[HISTORY_KEY]
            if (json != null) {
                gson.fromJson(json, listType)
            } else emptyList()
        }
    }
}