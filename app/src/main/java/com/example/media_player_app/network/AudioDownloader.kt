package com.example.media_player_app.network

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class AudioDownloader(
    val context: Context
):Downloader {


    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    override fun downloadFile(url: String, filename:String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("audio/mpeg")
            .setTitle("${filename}.mp3")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename)
        return downloadManager.enqueue(request)
    }
}