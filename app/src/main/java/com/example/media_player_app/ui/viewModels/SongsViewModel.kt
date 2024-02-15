package com.example.media_player_app.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.example.media_player_app.mediaPlayer.MusicPlayer
import com.example.media_player_app.model.Song
import com.example.media_player_app.repository.SongsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(
    val songsRepo: SongsRepository
): ViewModel() {

    // music files
    private var _songsList = MutableLiveData<List<Song>>()
    val songsList:LiveData<List<Song>>
        get() = _songsList


    // current playing song
    var songClicked = MutableLiveData<Song>()


    // repo calls
    fun getSongs(){
        viewModelScope.launch(Dispatchers.IO) {
            val data = songsRepo.loadMusicFiles()
            _songsList.postValue(data)
        }
    }

    fun clickSong(song:Song){
        songClicked.value = song
    }
}