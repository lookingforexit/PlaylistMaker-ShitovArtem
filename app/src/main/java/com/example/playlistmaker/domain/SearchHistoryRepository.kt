package com.example.playlistmaker.domain

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun addToHistory(str: String)
    fun getHistory(): Flow<List<String>>
}