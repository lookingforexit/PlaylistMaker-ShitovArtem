package com.example.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.history.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.network.Track
import com.example.playlistmaker.domain.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.coroutines.coroutineContext

sealed class SearchState {
    object Initial: SearchState() // Первоначальное cостояние экрана
    object Searching: SearchState() // Cостояние экрана при начале поиска
    data class Success(val foundList: List<Track>): SearchState() // Cостояние экрана при успешном завершении поиска
    data class Fail(val error: String): SearchState() // Cостояние экрана, если при запросе к серверу произошла ошибка
}

class SearchViewModel(
    private val tracksRepository: TracksRepository,
    val historyRepository: SearchHistoryRepositoryImpl
) : ViewModel() {
    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState  = _searchScreenState.asStateFlow()
    private var searchJob: Job? = null

    fun search(whatSearch: String) {
        searchJob?.cancel()

        if (whatSearch.isBlank()) {
            _searchScreenState.update { SearchState.Initial }
        }

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(300)
            executeSearch(whatSearch)
        }
    }

    private suspend fun executeSearch(whatSearch: String) {
        try {
            if (coroutineContext.isActive) {
                _searchScreenState.update { SearchState.Searching }
                val list = tracksRepository.searchTracks(expression = whatSearch)
                _searchScreenState.update { SearchState.Success(foundList = list) }
            }
        } catch (e: IOException) {
            if (coroutineContext.isActive) {
                _searchScreenState.update { SearchState.Fail(e.message.toString()) }
            }
        }
    }

    fun clearQuery() {
        searchJob?.cancel()
        _searchScreenState.update { SearchState.Initial }
    }

    fun searchAndAddToHistory(query: String) {
        if (query.isNotBlank()) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch(Dispatchers.IO) {
                historyRepository.addToHistory(query)
                executeSearch(query)
            }
        }
    }
}