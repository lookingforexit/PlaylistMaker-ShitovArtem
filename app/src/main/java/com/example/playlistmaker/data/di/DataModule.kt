package com.example.playlistmaker.data.di

import com.example.playlistmaker.creator.Storage
import com.example.playlistmaker.data.history.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.data.playlist.PlaylistsRepositoryImpl
import com.example.playlistmaker.domain.NetworkClient
import com.example.playlistmaker.domain.PlaylistsRepository
import com.example.playlistmaker.domain.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val dataModule = module {
    factory<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single {
        Storage()
    }

    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    single {
        SearchHistoryRepositoryImpl(get())
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl()
    }
}