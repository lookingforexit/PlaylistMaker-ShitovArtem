package com.example.playlistmaker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.playlistmaker.ui.screen.AddPlaylistScreen
import com.example.playlistmaker.ui.screen.FavoriteTracksScreen
import com.example.playlistmaker.ui.screen.MainScreen
import com.example.playlistmaker.ui.screen.PlaylistsScreen
import com.example.playlistmaker.ui.screen.SearchScreen
import com.example.playlistmaker.ui.screen.SettingsScreen
import com.example.playlistmaker.ui.screen.TrackScreen
import com.example.playlistmaker.ui.screen.TracksInPlaylistScreen
import com.example.playlistmaker.ui.viewmodel.AddPlaylistViewModel
import com.example.playlistmaker.ui.viewmodel.FavoriteTracksViewModel
import com.example.playlistmaker.ui.viewmodel.PlaylistsViewModel
import com.example.playlistmaker.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.ui.viewmodel.TrackViewModel
import com.example.playlistmaker.ui.viewmodel.TracksInPlaylistViewModel
import org.koin.compose.viewmodel.koinViewModel

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

    data object Favorite: ScreenRoute("favorite")
}

class PlaylistHost(
    private val navController: NavHostController
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

    private fun navigateToFavorite() {
        navController.navigate(ScreenRoute.Favorite.route)
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
                    onPlaylistsClick = { navigateToPlaylists() },
                    onFavoriteClick = { navigateToFavorite() }
                )
            }
            composable(ScreenRoute.Search.route) {
                val searchViewModel: SearchViewModel = koinViewModel()
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
                val playlistsViewModel: PlaylistsViewModel = koinViewModel()
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
                val trackViewModel: TrackViewModel = koinViewModel()
                LaunchedEffect(trackID) {
                    trackViewModel.getTrackByID(trackID)
                }
                TrackScreen(
                    trackViewModel = trackViewModel,
                    onBackClick = { navigateBack() }
                )
            }
            composable(ScreenRoute.AddPlaylist.route) {
                val addPlaylistViewModel: AddPlaylistViewModel = koinViewModel()
                AddPlaylistScreen(
                    addPlaylistViewModel = addPlaylistViewModel,
                    onBackClick = { navigateBack() }
                )
            }
            composable(ScreenRoute.TracksInPlaylist.route) { it ->
                val playlistID = it.arguments?.getString("playlistID")?.toInt() ?: 0
                val tracksInPlaylistViewModel: TracksInPlaylistViewModel = koinViewModel()
                LaunchedEffect(playlistID) {
                    tracksInPlaylistViewModel.getAllTracksInPlaylist(playlistID)
                }
                TracksInPlaylistScreen(
                    tracksInPlaylistViewModel = tracksInPlaylistViewModel,
                    playlistID = playlistID,
                    onBackClick = { navigateBack() },
                    onTrackClick = { navigateToTrack(it) }
                )
            }
            composable(route = ScreenRoute.Favorite.route) {
                val favoriteTracksViewModel: FavoriteTracksViewModel = koinViewModel()
                FavoriteTracksScreen(
                    favoriteTracksViewModel = favoriteTracksViewModel,
                    onBackClick = { navigateBack() },
                    onTrackClick = { navigateToTrack(it) },
                    onLongTrackClick = {  }
                )
            }
        }
    }
}