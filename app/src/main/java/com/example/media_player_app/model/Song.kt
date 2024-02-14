package com.example.media_player_app.model

import android.net.Uri

data class Song(
    val id : Long,
    val name: String,
    val artist: String,
    val duration:Int,
    val contentUri: Uri
)