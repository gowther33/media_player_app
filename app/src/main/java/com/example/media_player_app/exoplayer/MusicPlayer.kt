package com.example.media_player_app.exoplayer

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.media_player_app.model.Song

object MusicPlayer {

    private var exoPlayer : ExoPlayer? = null
    private var currentSong : Song? =null

    fun getCurrentSong() : Song?{
        return currentSong
    }

    fun getInstance() : ExoPlayer?{
        return exoPlayer
    }

    fun startPlaying(context : Context, song : Song){
        if(exoPlayer==null)
            exoPlayer = ExoPlayer.Builder(context).build()

        if(currentSong!=song){
            //Its a new song so start playing
            currentSong = song

            currentSong?.contentUri?.apply {
                val mediaItem = MediaItem.fromUri(this)
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.prepare()
                exoPlayer?.play()

            }
        }


    }
}