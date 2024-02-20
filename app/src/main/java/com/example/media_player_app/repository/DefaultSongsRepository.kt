package com.example.media_player_app.repository

import android.content.ComponentName
import android.content.ContentUris
import android.provider.MediaStore
import com.example.media_player_app.model.Song
import com.example.media_player_app.utils.sdk29AndUp
import android.content.ContentResolver
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.net.toUri
import com.example.media_player_app.network.AudioDownloader

class DefaultSongsRepository(
    private val context: Context
):SongsRepository {

    override suspend fun loadMusicFiles(): List<Song> {
        val songFiles = mutableListOf<Song>()
        val collection = sdk29AndUp {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } ?: MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
        )
        val data = context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            "${MediaStore.Audio.Media.DATE_ADDED} ASC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val displayNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val artistNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn)
                val artist = cursor.getString(artistNameColumn)
                val duration = cursor.getInt(durationColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                songFiles.add(Song(id, displayName, artist, duration, contentUri))
            }
            songFiles.toList()
        } ?: listOf()

        return data
    }


    private fun downloadFile(url:String, fileName: String) {
        if (checkInternetConnection()){
            val downloader = AudioDownloader(context)
            downloader.downloadFile(url, fileName)
        }else{
            Toast.makeText(context, "No Internet", Toast.LENGTH_LONG).show()
        }

    }

    private fun checkInternetConnection():Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    override suspend fun loadDownloadFiles(): List<Song> {
        val songFiles = mutableListOf<Song>()
        val collection = sdk29AndUp {
            MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } ?: MediaStore.Downloads.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Downloads._ID,
            MediaStore.Downloads.DISPLAY_NAME,
            MediaStore.Downloads.ARTIST,
            MediaStore.Downloads.DURATION,
        )
        val data = context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            "${MediaStore.Downloads.DATE_ADDED} ASC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID)
            val displayNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME)
            val artistNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn)
                val artist = cursor.getString(artistNameColumn)
                val duration = cursor.getInt(durationColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    id
                )
                songFiles.add(Song(id, displayName, artist, duration, contentUri))
            }
            songFiles.toList()
        } ?: listOf()

        return data
    }

    override fun playMusic(song: Song) {
        val fileExists = lookForFile(song.name)
        if (fileExists){
            Toast.makeText(context, "File Exists", Toast.LENGTH_SHORT).show()
        }else{
            downloadFile(song.contentUri.toString(), song.name)
//            Toast.makeText(context, "File Downloading...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun lookForFile(name:String):Boolean{
        val collection = sdk29AndUp {
            MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } ?: MediaStore.Downloads.EXTERNAL_CONTENT_URI


        val projection = arrayOf(MediaStore.Downloads.DISPLAY_NAME)
        val selection = "${MediaStore.Downloads.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(name)
        val cursor = context.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            null
        )

        val exists = cursor?.moveToFirst() ?: false
        cursor?.close()

        return exists
    }

    // hard coded data
    override fun getMusicFiles(): List<Song>{
        return listOf(
            Song(1L,
                "Yet Not I But Through Christ In Me",
                "Hillsong",
                5,
                "https://firebasestorage.googleapis.com/v0/b/media-player-c17dd.appspot.com/o/So%20Will%20I%20(100%20Billion%20X)%20-%20Hillsong%20Worship%20(64%20kbps).mp3?alt=media&token=d37f4915-fc16-4068-a6b7-9fb1d510e7a6".toUri()
            ),
            Song(2L,
                "Living Hope",
                "Phil Wickham",
                3,
                "https://firebasestorage.googleapis.com/v0/b/media-player-c17dd.appspot.com/o/Phil%20Wickham%20-%20Living%20Hope%20(Official%20Music%20Video)%20(64%20kbps).mp3?alt=media&token=9ea7d217-9e8d-40dc-9dd1-0cfda47c792e".toUri()
            ),
            Song(1L,
                "You are the reason1",
                "Calum Scott",
                3,
                "https://firebasestorage.googleapis.com/v0/b/media-player-c17dd.appspot.com/o/Calum%20Scott%20-%20You%20Are%20The%20Reason%20(Official%20Video)%20(128%20kbps).mp3?alt=media&token=413f2912-1532-4553-a117-c6f66bd91415".toUri()
            )
        )
    }
}