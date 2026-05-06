package com.example.playlistmaker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.playlistmaker.ui.screen.AddPlaylistScreen
import com.example.playlistmaker.ui.screen.MainScreen
import com.example.playlistmaker.ui.screen.PlaylistsScreen
import com.example.playlistmaker.ui.screen.SearchScreen
import com.example.playlistmaker.ui.screen.SettingsScreen
import com.example.playlistmaker.ui.screen.TrackScreen
import com.example.playlistmaker.ui.screen.TracksInPlaylistScreen
import com.example.playlistmaker.ui.viewmodel.AddPlaylistViewModel
import com.example.playlistmaker.ui.viewmodel.PlaylistsViewModel
import com.example.playlistmaker.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.ui.viewmodel.TrackViewModel
import com.example.playlistmaker.ui.viewmodel.TracksInPlaylistViewModel

sealed class ScreenRoute(val route: String) {
    data object Main: ScreenRoute("main")
    data object Search: ScreenRoute("search")
    data object Settings: ScreenRoute("settings")
    data object Playlists: ScreenRoute("playlists")
    data object Track: ScreenRoute("track/{trackID}") {
        fun createRoute(trackID: Int): String {
            return "track/${trackID}"
        }
    }
    data object AddPlaylist: ScreenRoute("add_playlist")
    data object TracksInPlaylist: ScreenRoute("tracks_in_playlist/{playlistID}") {
        fun createRoute(playlistID: Int): String {
            return "tracks_in_playlist/${playlistID}"
        }
    }
}

class PlaylistHost(
    private val navController: NavHostController,
    private val searchViewModel: SearchViewModel,
    private val playlistsViewModel: PlaylistsViewModel,
    private val trackViewModel: TrackViewModel,
    private val addPlaylistViewModel: AddPlaylistViewModel,
    private val tracksInPlaylistViewModel: TracksInPlaylistViewModel
) {
    private fun navigateToSearch() {
        navController.navigate(ScreenRoute.Search.route)
    }

    private fun navigateToSettings() {
        navController.navigate(ScreenRoute.Settings.route)
    }

    private fun navigateBack() {
        navController.popBackStack()
    }

    private fun navigateToPlaylists() {
        navController.navigate(ScreenRoute.Playlists.route)
    }

    private fun navigateToTrack(trackID: Int) {
        navController.navigate(ScreenRoute.Track.createRoute(trackID))
    }

    private fun navigateToAddPlaylist() {
        navController.navigate(ScreenRoute.AddPlaylist.route)
    }

    private fun navigateToTracksInPlaylist(playlistID: Int) {
        navController.navigate(ScreenRoute.TracksInPlaylist.createRoute(playlistID))
    }

    @Composable
    fun NavGraph() {
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.Main.route
        ) {
            composable(ScreenRoute.Main.route) {
                MainScreen(
                    onSearchClick = { navigateToSearch() },
                    onSettingsClick = { navigateToSettings() },
                    onPlaylistsClick = { navigateToPlaylists() }
                )
            }
            composable(ScreenRoute.Search.route) {
                SearchScreen(
                    onClick = { navigateBack() },
                    modifier = Modifier.fillMaxSize(),
                    searchViewModel = searchViewModel,
                    onTrackClick = { navigateToTrack(trackID = it) }
                )
            }
            composable(ScreenRoute.Settings.route) {
                SettingsScreen(onClick = { navigateBack() })
            }
            composable(ScreenRoute.Playlists.route) {
                PlaylistsScreen(
                    modifier = Modifier,
                    playlistsViewModel = playlistsViewModel,
                    addNewPlaylist = { navigateToAddPlaylist() },
                    navigateToPlaylist = { navigateToTracksInPlaylist(playlistID = it) },
                    navigateBack = { navigateBack() }
                )
            }
            composable(ScreenRoute.Track.route) {
                val trackID = it.arguments?.getString("trackID")?.toInt() ?: 0
                LaunchedEffect(trackID) {
                    trackViewModel.getTrackByID(trackID)
                }
                TrackScreen(
                    trackViewModel = trackViewModel,
                    onBackClick = { navigateBack() }
                )
            }
            composable(ScreenRoute.AddPlaylist.route) {
                AddPlaylistScreen(
                    addPlaylistViewModel = addPlaylistViewModel,
                    onBackClick = { navigateBack() }
                )
            }
            composable(ScreenRoute.TracksInPlaylist.route) { it ->
                val playlistID = it.arguments?.getString("playlistID")?.toInt() ?: 0
                LaunchedEffect(playlistID) {
                    tracksInPlaylistViewModel.getAllTracksInPlaylist(playlistID)
                }
                TracksInPlaylistScreen(
                    tracksInPlaylistViewModel = tracksInPlaylistViewModel,
                    playlistID = playlistID,
                    onBackClick = { navigateBack() },
                    onTrackClick = { navigateToTrack(it.trackID) }
                )
            }
        }
    }
}