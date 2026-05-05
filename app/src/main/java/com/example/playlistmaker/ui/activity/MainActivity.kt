package com.example.playlistmaker.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.playlistmaker.PlaylistHost
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.ui.viewmodel.PlaylistsViewModel
import com.example.playlistmaker.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.ui.viewmodel.TrackViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val searchViewModel: SearchViewModel by viewModel()
    private val playlistsViewModel: PlaylistsViewModel by viewModel()
    private val trackViewModel: TrackViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaylistMakerTheme {
                val navController = rememberNavController()
                val playlistHost = PlaylistHost(
                    navController,
                    searchViewModel,
                    playlistsViewModel,
                    trackViewModel
                )
                playlistHost.NavGraph()
            }
        }
    }
}