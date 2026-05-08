package com.example.playlistmaker.data.di

import android.content.Context
import android.preference.PreferenceDataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.playlistmaker.data.database.AppDatabase
import com.example.playlistmaker.data.history.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.network.ITunesAPIService
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.data.playlist.ImageSaver
import com.example.playlistmaker.data.playlist.PlaylistsRepositoryImpl
import com.example.playlistmaker.domain.NetworkClient
import com.example.playlistmaker.domain.PlaylistsRepository
import com.example.playlistmaker.domain.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    factory<NetworkClient> {
        RetrofitNetworkClient(get())
    }
    val baseUrl = "https://itunes.apple.com"

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<ITunesAPIService> {
        get<Retrofit>().create(ITunesAPIService::class.java)
    }

    single {
        PreferenceDataStoreFactory.create(produceFile = { get<Context>().preferencesDataStoreFile("settings_preferences") })
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    single {
        SearchHistoryRepositoryImpl(get())
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get())
    }

    single {
        Room.databaseBuilder(
            get<Context>(),
            AppDatabase::class.java,
            "playlists_maker"
        ).fallbackToDestructiveMigration()
            .build()
    }

    single {
        ImageSaver(get())
    }
}