package com.example.playlistmaker.data.history

import com.example.playlistmaker.domain.SearchHistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchHistoryRepositoryImpl(val scope: CoroutineScope): SearchHistoryRepository {
    private val historyList = ArrayDeque<String>()
    private val _historyState = MutableStateFlow<List<String>>(emptyList())

    companion object {
        private const val MAX_HISTORY_SIZE = 10
    }

    override fun addToHistory(str: String) {
        scope.launch(Dispatchers.IO) {
            historyList.removeIf { it == str }
            historyList.addFirst(str)

            if (historyList.size > MAX_HISTORY_SIZE) {
                historyList.removeLast()
            }
            _historyState.value = historyList.toList()
        }
    }

    override fun getHistory(): Flow<List<String>> = _historyState.asStateFlow()
}