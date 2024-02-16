package com.example.media_player_app.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class DownloadCompletedReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.DOWNLOAD_COMPLETE"){
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if (id != -1L){
                Toast.makeText(context, "File downloaded successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }
}