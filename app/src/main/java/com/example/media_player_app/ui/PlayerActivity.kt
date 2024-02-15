package com.example.media_player_app.ui

import android.Manifest
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.media_player_app.databinding.ActivityPlayerBinding
import com.example.media_player_app.mediaPlayer.MusicPlaybackService
import com.example.media_player_app.mediaPlayer.MusicPlayer
import com.example.media_player_app.model.Song
import com.example.media_player_app.ui.base.BaseActivity
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

// Requires exoplayer
class PlayerActivity : BaseActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var controllerFuture: ListenableFuture<MediaController>

    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>

    private var playerListener = object : Player.Listener{
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
//            showGif(isPlaying)
        }
    }

     @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


         notificationPermissionLauncher = registerForActivityResult(
             ActivityResultContracts.RequestPermission()
         ) { isGranted: Boolean ->
             if (isGranted) {
                 Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
             } else {
                 Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show()
             }
         }

         if (hasNotificationPermission().not()){
             askPermission()
         }

         val song = intent?.getParcelableExtra<Song>("SONG")

        MusicPlayer.getCurrentSong()?.apply {
            binding.songTitleTextView.text = name
        }

        MusicPlayer.startPlaying(this, song!!)
        binding.playerView.player = exoPlayer
        binding.playerView.showController()
        exoPlayer.addListener(playerListener)

    }

    override fun onStart() {
        super.onStart()

        val sessionToken = SessionToken(this, ComponentName(this, MusicPlaybackService::class.java))
        controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        val factory = MediaController.Builder(
            this,
            sessionToken
        ).buildAsync()
        factory.addListener(
            {
                // MediaController is available here with controllerFuture.get()
                mediaController = factory?.let{
                    if (it.isDone){
                        it.get()
                    }
                    else{
                        null
                    }
                }
            },
            MoreExecutors.directExecutor()
        )
    }

    private fun hasNotificationPermission():Boolean{
        return checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }

    private fun askPermission(){
        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    override fun onStop() {
        super.onStop()
        MediaController.releaseFuture(controllerFuture)
    }


    override fun onDestroy() {
        super.onDestroy()
//        exoPlayer.release()
//        exoPlayer?.removeListener(playerListener)
    }
}
