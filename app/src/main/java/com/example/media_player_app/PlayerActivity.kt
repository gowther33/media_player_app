package com.example.media_player_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.media_player_app.databinding.ActivityPlayerBinding
import com.example.media_player_app.exoplayer.MusicPlayer

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var exoPlayer:ExoPlayer

    var playerListener = object : Player.Listener{
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
//            showGif(isPlaying)
        }
    }

    @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MusicPlayer.getCurrentSong()?.apply {
            binding.songTitleTextView.text = name
        }

        exoPlayer = MusicPlayer.getInstance()!!
        binding.playerView.player = exoPlayer
        binding.playerView.showController()
        exoPlayer.addListener(playerListener)


    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.removeListener(playerListener)
    }
}