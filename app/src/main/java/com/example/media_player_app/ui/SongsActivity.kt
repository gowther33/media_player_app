package com.example.media_player_app.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.media_player_app.adapter.MusicAdapter
import com.example.media_player_app.databinding.ActivitySongsBinding
import com.example.media_player_app.model.Song
import com.example.media_player_app.ui.base.BaseActivity
import com.example.media_player_app.ui.viewModels.SongsViewModel
import com.example.media_player_app.utils.sdk32AndDown
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SongsActivity : BaseActivity() {

    private lateinit var binding: ActivitySongsBinding

    private lateinit var permissionsLauncher: ActivityResultLauncher<String>

    private val viewModel:SongsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        permissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getData()
            } else {
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.songsList.observe(this){
            setupRecyclerView(it)
        }

        viewModel.songClicked.observe(this){song->
            Intent(this, PlayerActivity::class.java).also {
                it.putExtra("SONG",song)
                startActivity(it)
            }
        }

        // Check if has permission
        if (sdk32AndDown()){
            // check permission
            if (hasPermission()){
                getData()
            }else{
                val permissionString = Manifest.permission.READ_EXTERNAL_STORAGE
                askPermission(permissionString)
            }
        }else{
            getData()
        }
    }

    private fun setupRecyclerView(songsList:List<Song>) {
        val recyclerView = binding.recyclerView
        val adapter = MusicAdapter(viewModel)
        val layout = LinearLayoutManager(this)

        adapter.songList = songsList
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layout
    }

    private fun getData(){
        viewModel.getSongs()
    }

    private fun askPermission(permissionString:String){
        permissionsLauncher.launch(permissionString)
    }


    private fun hasPermission():Boolean{
        // If api level < 32 check permission
        return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

}