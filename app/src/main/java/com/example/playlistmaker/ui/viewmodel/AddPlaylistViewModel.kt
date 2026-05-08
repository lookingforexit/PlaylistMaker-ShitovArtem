package com.example.playlistmaker.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.playlist.ImageSaver
import com.example.playlistmaker.domain.PlaylistsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddPlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository,
    private val imageSaver: ImageSaver
): ViewModel() {
    private val _playlistName = MutableStateFlow("")
    val playlistName = _playlistName.asStateFlow()

    private val _playlistDescription = MutableStateFlow("")
    val playlistDescription = _playlistDescription.asStateFlow()

    private val _selectedImage = MutableStateFlow<Uri?>(null)
    val selectedImage = _selectedImage.asStateFlow()


    fun setSelectedImage(uri: Uri?) {
        _selectedImage.update { uri }
    }

    fun setPlaylistName(name: String) {
        _playlistName.update { name }
    }
    fun setPlaylistDescription(description: String) {
        _playlistDescription.update { description }
    }

    fun savePlaylist(onSaved: () -> Unit) {
        viewModelScope.launch {
            val uri = _selectedImage.value
            val image = uri?.let { imageSaver.saveImageToInternalStorage(it.toString()) }
            playlistsRepository.addPlaylist(
                name = _playlistName.value,
                description = _playlistDescription.value,
                image = image
            )
            onSaved()
        }

    }
}
