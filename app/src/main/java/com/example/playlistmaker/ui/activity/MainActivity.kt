package com.example.playlistmaker.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.playlistmaker.PlaylistHost
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.ui.viewmodel.AddPlaylistViewModel
import com.example.playlistmaker.ui.viewmodel.PlaylistsViewModel
import com.example.playlistmaker.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.ui.viewmodel.TrackViewModel
import com.example.playlistmaker.ui.viewmodel.TracksInPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaylistMakerTheme {
                val navController = rememberNavController()
                val playlistHost = PlaylistHost(navController)
                playlistHost.NavGraph()
            }
        }
    }
}