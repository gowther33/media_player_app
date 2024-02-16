package com.example.media_player_app.repository

import com.example.media_player_app.model.Song

interface SongsRepository {
    suspend fun loadMusicFiles():List<Song>


    fun downloadFile(url:String)

}