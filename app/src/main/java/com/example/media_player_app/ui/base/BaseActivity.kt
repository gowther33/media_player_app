package com.example.media_player_app.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.media3.exoplayer.ExoPlayer
import com.example.media_player_app.model.Song
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity:AppCompatActivity() {

    @Inject
    lateinit var exoPlayer: ExoPlayer

//    var songPlaying: Song? = null

}