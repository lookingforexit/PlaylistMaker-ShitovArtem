package com.example.playlistmaker.ui.di

import com.example.playlistmaker.ui.viewmodel.AddPlaylistViewModel
import com.example.playlistmaker.ui.viewmodel.FavoriteTracksViewModel
import com.example.playlistmaker.ui.viewmodel.PlaylistsViewModel
import com.example.playlistmaker.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.ui.viewmodel.TrackViewModel
import com.example.playlistmaker.ui.viewmodel.TracksInPlaylistViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }
    viewModel {
        PlaylistsViewModel(get())
    }
    viewModel {
        TrackViewModel(get(), get())
    }
    viewModel {
        AddPlaylistViewModel(get(), get())
    }
    viewModel {
        TracksInPlaylistViewModel(get())
    }
    viewModel {
        FavoriteTracksViewModel(get())
    }
}