package com.example.media_player_app

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.media_player_app.adapter.MusicAdapter
import com.example.media_player_app.databinding.ActivityMainBinding
import com.example.media_player_app.model.Song
import com.example.media_player_app.utils.sdk29AndUp
import com.example.media_player_app.utils.sdk32AndDown

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var permissionsLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        permissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                setupRecyclerView()
            } else {
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show()
            }
        }

        // Check if has permission
        if (sdk32AndDown()){
            // check permission
            if (hasPermission()){
                setupRecyclerView()
            }else{
                val permissionString = Manifest.permission.READ_EXTERNAL_STORAGE
                askPermission(permissionString)
            }
        }else{
            setupRecyclerView()
        }

    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        val adapter = MusicAdapter(this)
        val layout = LinearLayoutManager(this)

        var data = loadMusicFiles()
        adapter.songList = data
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layout

    }


    private fun loadMusicFiles(): List<Song> {
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
        var data = contentResolver.query(
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

    private fun askPermission(permissionString:String){
        permissionsLauncher.launch(permissionString)
    }


    private fun hasPermission():Boolean{
        // If api level < 32 check permission
        return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
}