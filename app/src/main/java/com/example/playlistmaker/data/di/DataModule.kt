package com.example.playlistmaker.data.di

import com.example.playlistmaker.creator.Storage
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.domain.NetworkClient
import com.example.playlistmaker.domain.TracksRepository
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
}