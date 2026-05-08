package com.example.playlistmaker.domain

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    suspend fun addToHistory(str: String)
    fun getHistory(): Flow<List<String>>
}